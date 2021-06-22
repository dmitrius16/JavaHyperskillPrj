# Write your code here

import pandas as pd
import re

def get_input_file():
    '''
    Get input file name
    return tuple (file_name, extension)
    '''
    print("Input file name")
    return input().split('.')


def clean_data(dataFrame):
    '''
    clean_data - cleans DataFrame, makes only integers in cells
    :param dataFrame:
    :return: num corrected cells
    '''
    cnt = 0
    for col in dataFrame.columns:
        for ind, elem in enumerate(dataFrame[col]):
            temp = re.sub("\D", "", elem)
            if temp != elem:
                cnt += 1
                dataFrame[col][ind] = temp
    return cnt






def main():
    '''main method'''
    # src_file_folder = "../test/"
    # src_file_name = 'data_big_csv'
    #file_name, file_ext = src_file_folder + src_file_name,'csv'# get_input_file()
    file_name, file_ext = get_input_file()
    full_file_name = f'{file_name}.{file_ext}'

    data = pd.read_excel(full_file_name,
                         sheet_name='Vehicles',
                         dtype='str') if file_ext == "xlsx" \
        else pd.read_csv(full_file_name, dtype='str')

    if file_ext == 'xlsx':
        data.to_csv(f'{file_name}.csv', index=None, header=True)
        print(f'{data.shape[0]} line{"s were" if data.shape[0] > 1 else " was"} imported to {file_name}.csv')

    n_corr_cells = clean_data(data)
    corr_file_name = f'{file_name}[CHECKED].csv'
    print(f'{n_corr_cells} cell{"s were" if n_corr_cells > 1 else " was"} corrected in {corr_file_name}')
    data.to_csv(corr_file_name, index=None, header=True)


    '''
    if file_ext == "xlsx":
        data = pd.read_excel(full_file_name, sheet_name='Vehicles', dtype='str')
        clean_data(data)
        data.to_csv(f'{file_name}.csv', index=None, header=True)
        print(f'{data.shape[0]} line{"s were" if data.shape[0] > 1 else " was"} imported to {file_name}.csv')
    elif file_ext == "csv":
        data = pd.read_csv(full_file_name, dtype='str')
        n_corr_cells = clean_data(data)
        print(f'{n_corr_cells} cell{"s were" if n_corr_cells > 1 else " was"} corrected')
        data.to_csv(f'{file_name}.csv', index=None, header=True)
        # data.to_csv(f'{src_file_name}.csv', index=None, header=True) # clear it
    '''



if __name__ == '__main__':
    main()
