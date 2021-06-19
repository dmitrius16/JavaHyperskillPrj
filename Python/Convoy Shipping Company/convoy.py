# Write your code here

import pandas as pd


def get_input_file():
    '''
    Get input file name
    return tuple (file_name, extension)
    '''
    print("Input file name")
    return input().split('.')

def main():
    '''main method'''
    file_name, file_ext = get_input_file()
    data = pd.read_excel(f'{file_name}.{file_ext}',sheet_name='Vehicles')
    data.to_csv(f'{file_name}.csv', index=None, header=True)

if __name__ == '__main__':
    main()
