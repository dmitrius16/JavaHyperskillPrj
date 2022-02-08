# Write your code here
from tetris_pieces import BaseFigure
import fabric_tetris_shape


class TetrisGame:
    def __init__(self):
        self.__field_size = [int(x) for x in input().strip().split(" ")]
        self.__shape = None
        self.__shape_cmds = {"down": BaseFigure.move_down,
                             "right": BaseFigure.move_right,
                             "left": BaseFigure.move_left,
                             "rotate": BaseFigure.rotate}
        BaseFigure.create_field(self.__field_size)

    def user_dialog_loop(self):
        """
        :return:
        """
        user_cmd = ""
        while True:
            user_cmd = input()
            if user_cmd == "exit":
                break
            elif user_cmd == "piece":
                self.__shape = fabric_tetris_shape.create_tetris_pieces(input())
                self.__shape.draw()
            elif user_cmd == "break":
                BaseFigure.break_cmd()
            else:
                if not self.execute_user_cmd(user_cmd):
                    break

    def execute_user_cmd(self, cmd_name):
        """
        :param cmd_name:
        :return:
            True - continue receive commands
            False - game over occur
        """
        cmd = self.__shape_cmds.get(cmd_name)
        res = True
        if cmd:
            if self.__shape:
                res = self.__shape.exec_cmd(cmd)
        else:
            print("Please input right command")
        return res


if __name__ == "__main__":
    game = TetrisGame()
    game.user_dialog_loop()
