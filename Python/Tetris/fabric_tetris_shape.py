"""
This module creates tetris shapes
"""
import tetris_pieces


def create_tetris_pieces(name):
    """
    Creates tetris peaces according passed name
    Args:
         name `str`: Name tetris figure. Now supported 7 names:
         I, S, Z, L, J, O, T
     Returns:
         `BaseFigure`: tetris figure.
    """
    name = name.upper()
    if name == "I":
        return tetris_pieces.FigureI()
    elif name == "S":
        return tetris_pieces.FigureS()
    elif name == "Z":
        return tetris_pieces.FigureZ()
    elif name == "L":
        return tetris_pieces.FigureL()
    elif name == "J":
        return tetris_pieces.FigureJ()
    elif name == "O":
        return tetris_pieces.FigureO()
    elif name == "T":
        return tetris_pieces.FigureT()
    else:
        raise RuntimeError("Unsupported figure")
