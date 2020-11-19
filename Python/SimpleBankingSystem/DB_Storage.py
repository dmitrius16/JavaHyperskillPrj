import sqlite3
from sqlite3 import Error
from pathlib import Path

def db_create_connection():
    db_conn = None
    try:
        db_file = Path(r".\card.s3db")
        db_conn = sqlite3.connect(r'.\card.s3db')
        #if not db_file.is_file(): # we need to create table
        if db_conn is not None:
            db_create_table(db_conn)

    except Error as e:
        print(e)
    return db_conn


def db_create_table(db_connection):
    sql_create_card_table = '''CREATE TABLE IF NOT EXISTS card (
                                id INTEGER primary key autoincrement,
                                number TEXT,
                                pin TEXT,
                                balance INTEGER DEFAULT 0
                                );'''

    try:
        curs = db_connection.cursor()
        curs.execute(sql_create_card_table)
        db_connection.commit()
    except Error as e:
        print(e)


def db_add_account(db_connection, card_num, pin_code):
    sql_insert_account = f'''INSERT INTO card (number, pin) VALUES('{card_num}','{pin_code}')'''

    try:
        curs = db_connection.cursor()
        curs.execute(sql_insert_account)
        db_connection.commit()
    except Error as e:
        print(e)


def db_check_account(db_connection, card_num, pin_code):
    sql_query = f"SELECT number, pin, balance FROM card WHERE number = '{card_num}' and pin = '{pin_code}'"

    try:
        curs = db_connection.cursor()
        curs.execute(sql_query)
        return curs.fetchone()
    except Error as e:
        print(e)
        return None


def db_check_card(db_connection, card_num):
    sql_query = f'''SELECT number, balance FROM card WHERE number = {card_num}'''

    try:
        curs = db_connection.cursor()
        curs.execute(sql_query)
        return curs.fetchone()
    except Error as e:
        print(e)
        return None


def db_change_balance(db_connection, credit_card):
    sql_query = f'UPDATE card SET balance = {credit_card.balance} WHERE number = {credit_card.card_num}'
    result = False
    try:
        curs = db_connection.cursor()
        curs.execute(sql_query)
        db_connection.commit()
        result = True
    except Error as e:
        print(e)
    finally:
        return result


def db_get_accounts(db_connection):
    sql_query = f'''SELECT number FROM card'''
    try:
        curs = db_connection.cursor()
        curs.execute(sql_query)
        return curs.fetchall()
    except Error as e:
        print(e)
        return None


def db_delete_account(db_connection, credit_card):
    sql_query = f'DELETE FROM card WHERE number = {credit_card.card_num}'
    result = False
    try:
        curs = db_connection.cursor()
        curs.execute(sql_query)
        db_connection.commit()
        result = True
    except Error as e:
        print(e)
    finally:
        return result
