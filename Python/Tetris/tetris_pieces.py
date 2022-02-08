"""
This module creates tetris pieces or figures.
Figures are placed on field 4x4
"""
import numpy as np


class BaseFigure:
    """
    BaseFigure class for tetris figure
    """
    empty_cell = '-'
    fill_cell = '0'
    field_shape = 20, 10
    field = None
    lin_field = None
    filled_line = None

    @staticmethod
    def create_field(shape):
        """
        Creates field for peaces. Now is supported 20x10 and 24x10
        Args:
            shape `typle`: size of field.
        """
        num_str = shape[1]
        num_col = 10  # shape[0] 10 col because all pieces created as 10 length

        BaseFigure.field_shape = (num_str, num_col)
        BaseFigure.field = np.full((num_str, num_col), BaseFigure.empty_cell)
        BaseFigure.lin_field = BaseFigure.field.reshape(num_col * num_str)
        BaseFigure.filled_line = np.full(num_col, BaseFigure.fill_cell)
        BaseFigure.empty_line = np.full(num_col, BaseFigure.empty_cell)
        # for debug purposes
        """
        BaseFigure.field[-1] = BaseFigure.filled_line
        BaseFigure.field[-2][0] = BaseFigure.fill_cell

        BaseFigure.field[-2][-1] = BaseFigure.fill_cell
        BaseFigure.field[-3] = BaseFigure.filled_line
        BaseFigure.field[-4] = BaseFigure.filled_line
        """
        # clear debug purposes
        BaseFigure.draw_field()

    @staticmethod
    def draw_field():
        """
        Draws current state of field
        :return:
        """
        for i in range(BaseFigure.field_shape[0]):
            st = i * BaseFigure.field_shape[1]
            end = st + BaseFigure.field_shape[1]
            print(" ".join(BaseFigure.lin_field[st:end]))
        print()

    @staticmethod
    def break_cmd():
        """
        if there is filled line, clears it
        """
        st_ind = BaseFigure.field_shape[0] - 1
        while st_ind > 0:
            if (BaseFigure.field[st_ind] == BaseFigure.filled_line).all():
                # make copy
                if st_ind > 0:
                    for i in range(st_ind - 1, 0, -1):
                        BaseFigure.field[i + 1] = BaseFigure.field[i]
                    st_ind = BaseFigure.field_shape[0] - 1
                BaseFigure.field[0] = BaseFigure.empty_line
                continue
            st_ind -= 1

        BaseFigure.draw_field()

    def __init__(self, rotate_positions, right_bounds, left_bounds):
        """
        Args:
            rotate_positions `np.array`: figure rotate position
            right_bounds `typle`: figure right bound indexes
        Initialization
        Attributes:
            __rotate_state - curent rotate position
            __max_rotate_state - max rotate position count. We define this field in derived classes
            __positions - figure bluprints in rotate position. Defined in derived classes
        """
        self.__positions = rotate_positions
        self.__right_bounds = right_bounds
        self.__left_bounds = left_bounds
        self.__max_rotate_state = len(rotate_positions)
        self.__rotate_state = 0
        self.__static_state = False

    def rotate(self):
        """
        Rotate shape
        """
        if not self.__static_state:
            self.clear_figure()
            self.__rotate_state = (self.__rotate_state + 1) % self.__max_rotate_state
            self.move_down()

    def move_left(self):
        """
        Moves figure to left
        """
        if not self.__static_state:
            for i, pos in enumerate(self.__positions):
                if self.__is_can_move_left(pos, i):
                    if i == self.__rotate_state:
                        self.clear_figure()
                    pos -= 1
                if i == self.__rotate_state:
                    self.move_down()

    def move_right(self):
        """
        Moves figure to right
        """
        if not self.__static_state:
            for i, pos in enumerate(self.__positions):
                if self.__is_can_move_right(pos, i):
                    if i == self.__rotate_state:
                        self.clear_figure()
                    pos += 1
                if i == self.__rotate_state:
                    self.move_down()

    def move_down(self):
        """
        Moves figure down
        """
        if not self.__static_state:
            for i, pos in enumerate(self.__positions):
                if self.__is_can_move_down(pos):
                    if i == self.__rotate_state:
                        self.clear_figure()
                    pos += BaseFigure.field_shape[1]
                else:  # if pieces touches other pieces
                    if i == self.__rotate_state:
                        self.__static_state = True

    def exec_cmd(self, cmd_func):
        """
        Executes one command from set: move_left, move_right, rotate, move_down
        Args:
            cmd_func `func`: func from set: move_left, move_right, move_down, rotate
        Returns:
            `bool`: True - command executed and ready for new command
                    False - game over occur
        """
        cmd_func(self)
        self.draw()
        res = True
        if self.__is_game_over():
            print("Game Over!")
            res = False
        return res

    def draw(self):
        """
        Draw tetris peaces
        """
        fig_shape = self.__positions[self.__rotate_state]
        for i in fig_shape:
            BaseFigure.lin_field[i] = BaseFigure.fill_cell
        BaseFigure.draw_field()

    def clear_figure(self):
        """
        Clears figure on current position
        """
        fig_shape = self.__positions[self.__rotate_state]
        for i in fig_shape:
            BaseFigure.lin_field[i] = BaseFigure.empty_cell

    def clear_field(self):
        """
        Clear field
        """
        BaseFigure.lin_field.fill(BaseFigure.empty_cell)

    def make_full_rotate(self):
        """
        Rotates figure until full rotate
        """
        self.draw()
        for i in range(5):
            self.rotate()
            self.draw()

    def __is_can_move_down(self, pos):
        """
        Checks if the figure can be moved to the down
        Args:
            pos `ndarray` - figure shape
        Returns
            `bool`: True if figure can be moved to the down
                    False if figure on the floor
        """
        res = True
        new_pos = pos + BaseFigure.field_shape[1]
        res = (new_pos < BaseFigure.lin_field.size).all()
        if res:
            set_indexes = (new_pos // 10)  # here we select indexes on bottom line
            set_indexes = (set_indexes == set_indexes.max()).nonzero()[0]
            res = (BaseFigure.lin_field.take(new_pos.take(set_indexes)) != BaseFigure.fill_cell).all()
        return res

    def __is_can_move_right(self, pos, fictive_rotate_state):
        """
        Rewrite
        Checks if the figure can be moved to the right
        Args:
            pos `ndarray` - figure shape
            fictive_rotate_state - index rotate state
        Returns:
            `bool`: True if the figure can be moved to the right
                    False if the figure touches the right border
        """
        res = False
        new_pos = pos % BaseFigure.field_shape[1]
        if (new_pos < BaseFigure.field_shape[1] - 1).all():  # check if we don't touch right bound
            res = True
            new_pos = pos + 1  # move right
            for i in self.__right_bounds[fictive_rotate_state]:
                if BaseFigure.lin_field[new_pos[i]] == BaseFigure.fill_cell:
                    res = False
                    break
        return res

    def __is_can_move_left(self, pos, fictive_rotate_state):
        """
        Rewrite
        Checks if the figure can be moved to the left
        Args:
            pos `ndarray` - figure shape
        Returns:
            `bool`: True if the figure can be moved to the left
                    False if the figure touches the left border
        """
        res = False
        new_pos = pos % BaseFigure.field_shape[1]
        if (new_pos > 0).all():  # check if we don't touch right bound
            res = True
            new_pos = pos - 1  # move right
            for i in self.__left_bounds[fictive_rotate_state]:
                if BaseFigure.lin_field[new_pos[i]] == BaseFigure.fill_cell:
                    res = False
                    break
        return res

    def __is_can_rotate(self):
        res = not self.__static_state
        if res:
            pass
        return res

    def __is_cell_free(self, cell_ind):
        """
        Checks if the cell has already busied
        Args:
            cell_ind `int`: cell index in linear field representation
        Returns:
            `bool`: True if cell is free, otherwise False
        """
        res = BaseFigure.lin_field[cell_ind] == BaseFigure.fill_cell
        print(f"Cell with index {cell_ind} equal {BaseFigure.lin_field[cell_ind]}")
        return res

    def __is_game_over(self):
        """
        Checks if vertical column filled then game over
        Returns:
            `bool`: True - game over
                    False - game is continue
        """
        return (BaseFigure.field[0] == BaseFigure.fill_cell).any()


class FigureO(BaseFigure):
    """
    Tetris O shape
    """

    def __init__(self):
        pos = np.array([[4, 14, 15, 5]])
        right_bound_indexes = ((3, 2),)
        left_bound_indexes = ((0, 1),)
        super().__init__(pos, right_bound_indexes, left_bound_indexes)


class FigureI(BaseFigure):
    """
    Tetris I shape
    """

    def __init__(self):
        pos = np.array([[4, 14, 24, 34], [3, 4, 5, 6]])
        right_bound_indexes = ((0, 1, 2, 3), (3, ))
        left_bound_indexes = ((0, 1, 2, 3), (0, ))
        super().__init__(pos, right_bound_indexes, left_bound_indexes)


class FigureS(BaseFigure):
    """
    Tetris S shape
    """

    def __init__(self):
        pos = np.array([[5, 4, 14, 13], [4, 14, 15, 25]])
        rigth_bound_indexes = ((0, 2), (0, 2, 3))
        left_bound_indexes = ((1, 3), (0, 1, 3))
        super().__init__(pos, rigth_bound_indexes, left_bound_indexes)


class FigureZ(BaseFigure):
    """
    Tetris shape Z
    """

    def __init__(self):
        pos = np.array([[4, 5, 15, 16], [5, 15, 14, 24]])
        right_bound_indexes = ((1, 3), (0, 1, 3))
        left_bound_indexes = ((0, 2), (0, 2, 3))
        super().__init__(pos, right_bound_indexes, left_bound_indexes)


class FigureL(BaseFigure):
    """
    Tetris shape L
    """

    def __init__(self):
        pos = np.array([[4, 14, 24, 25], [5, 15, 14, 13], [4, 5, 15, 25], [6, 5, 4, 14]])
        right_bound_indexes = ((0, 1, 3), (0, 1), (1, 2, 3), (0, 3))
        left_bound_indexes = ((0, 1, 2), (0, 3), (0, 2, 3), (2, 3))
        super().__init__(pos, right_bound_indexes, left_bound_indexes)


class FigureJ(BaseFigure):
    """
    Tetris shape J
    """

    def __init__(self):
        pos = np.array([[5, 15, 25, 24], [15, 5, 4, 3], [5, 4, 14, 24], [4, 14, 15, 16]])
        right_bound_indexes = ((0, 1, 2), (0, 1), (0, 2, 3), (0, 3))
        left_bound_indexes = ((0, 1, 3), (0, 3), (1, 2, 3), (0, 1))
        super().__init__(pos, right_bound_indexes, left_bound_indexes)


class FigureT(BaseFigure):
    """
    Tetris shape T
    """

    def __init__(self):
        pos = np.array([[4, 14, 24, 15], [4, 13, 14, 15], [5, 15, 25, 14], [4, 5, 6, 15]])
        right_bound_indexes = ((0, 3, 2), (0, 3), (0, 1, 2), (2, 3))
        left_bound_indexes = ((0, 1, 2), (0, 1), (0, 3, 2), (0, 3))
        super().__init__(pos, right_bound_indexes, left_bound_indexes)
