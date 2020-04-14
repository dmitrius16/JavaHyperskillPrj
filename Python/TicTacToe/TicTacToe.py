# write your code here
def show_game_field(state_str):
    head_str = "---------"
    print(head_str)
    state_str = state_str.replace("_"," ")
    for i in range(0, 3):
        print("| " + " ".join(state_str[i * 3:i * 3 + 3]) + " |")
    print(head_str)

'''
    calc_X_or_o - calculate num of X or Y
'''


def calc_X_or_O(state_str, sym="O"):
    cnt = 0
    for ch in state_str:
        if ch == sym:
            cnt += 1
    return cnt

'''
    create_field - creates play field 
'''

def create_field(state_str):
    field = []
    field.extend([[x for x in state_str[6:9]]])
    field.extend([[x for x in state_str[3:6]]])
    field.extend([[x for x in state_str[0:3]]])
    return field

'''
    convert field to string
'''

def convert_field2Str():
    return "".join(["".join(game_field[el]) for el in range(2,-1,-1)])


'''
    fields_is_correct - check field for valid and return cortage (valid - True/False, free items)
'''


def field_is_correct(state_str):
    cntX = calc_X_or_O(state_str, sym="X")
    cntY = calc_X_or_O(state_str, sym="O")
    return abs(cntX - cntY) < 2, 9 - (cntX + cntY)


'''
    get_field_results - return list with game results: 3 horizontals, 3 verticals and 2 diagonals
'''


def get_field_results(state_str):
    res = []
    temp_res = [state_str[ind: ind + 3] for ind in (0, 3, 6)]  # horiz results
    res.extend(temp_res)
    temp_res = [state_str[ind: 9: 3] for ind in range(0, 3)]  # vertical results
    res.extend(temp_res)
    temp_res = state_str[0: 9: 4]  # first diagonal
    res.extend([temp_res])
    temp_res = state_str[2: 8: 2]  # second diagonal
    res.extend([temp_res])
    return res

'''
    get winner - return string with game result
'''

def get_winner(state_str):
    result_str = "Draw"
    free_field = calc_free_field()
    field_state = get_field_results(state_str)
    if "OOO" in field_state:
        result_str = "O Wins"
    elif "XXX" in field_state:
        result_str = "X Wins"
    elif free_field > 0:
        result_str = "Game not finished"
    return result_str

'''
    calc_free_field - calculate free cells
'''

def calc_free_field():
    num = 0
    for i in range(0, len(game_field)):
        for j in range(0, len(game_field[0])):
            if game_field[i][j] == "_":
                num += 1
    return num

'''
    is_cell_busy - check cell in field for busy
'''

def is_cell_free(x,y):
    return game_field[y][x] == "_"


'''
    ask_next_step - ask coordinate for next step
'''

def ask_next_step():
    while True:
        try:
            x, y = (int(el) for el in input("Enter the coordinates: ").split())
            if 1 <= x <= 3 and 1 <= y <= 3:
                x = x - 1
                y = y - 1
                global game_field
                if is_cell_free(x, y):
                    game_field[y][x] = step
                    break
                else:
                    print("This cell is occupied! Choose another one!")
            else:
                print("Coordinates should be from 1 to 3!")
        except ValueError:
            print("You should enter numbers!")

usrinp = "___" * 3
show_game_field(usrinp)
game_field = create_field(usrinp)
field_correct, free_elem = field_is_correct(usrinp)
step = "X"
if field_correct:
    while True:
        ask_next_step()
        step = "X" if step == "O" else "O"
        field = convert_field2Str()
        show_game_field(field)
        res = get_winner(field)
        if res != "Game not finished":
            break
    print(res)
else:
    print("Impossible")
