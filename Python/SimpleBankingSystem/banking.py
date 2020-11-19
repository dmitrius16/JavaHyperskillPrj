# Write your code here
import credit_card
import DB_Storage


class BankingService:
    def __init__(self):
        self.working_session = True
        self.main_menu = {1: ("Create an account", self.create_account), 2: ("Log into account", self.log_into_account), 0: ("Exit", self.exit) }
        self.account_menu = {1: ("Balance", self.get_balance),
                             2: ("Add income", self.add_income),
                             3: ("Do transfer", self.do_trasfer),
                             4: ("Close account", self.close_account),
                             5: ("Log out", self.log_out),
                             0: ("Exit", self.exit)}
        self.current_menu = self.main_menu
        self.current_account = None
        self.db_connect = DB_Storage.db_create_connection()
        if self.db_connect is not None:
            self.card_generator = credit_card.CreditCardManager(self.db_connect)

    def __check_account(self, card_num, card_pin):
        res = DB_Storage.db_check_account(self.db_connect, card_num, card_pin)
        if res is not None:
            return True, credit_card.CreditCard(*res)
        return False, None

    @staticmethod
    def __input_int(msg=""):
        while True:
            try:
                val = int(input(msg))
                break
            except ValueError:
                print("You need type number")
        return val

    def create_account(self):
        new_credit_card = credit_card.create_credit_card(self.card_generator)
        DB_Storage.db_add_account(self.db_connect, new_credit_card.card_num, new_credit_card.pin_code)
        print("Your card has been created", "Your card number:", new_credit_card.card_num, "Your card PIN:", new_credit_card.pin_code, sep='\n')

    def log_into_account(self):
        print("Enter your card number")
        card_num_inp = input()
        print("Enter your PIN:")
        card_pin_inp = input()
        res, acc = self.__check_account(card_num_inp, card_pin_inp)
        if res:
            print("You have successfully logged in!")
            self.current_menu = self.account_menu
            self.current_account = acc
        else:
            print("Wrong card number or PIN!")

    def log_out(self):
        print("You have successfully logged out")
        self.current_account = None
        self.current_menu = self.main_menu

    def get_balance(self):
        print("Balance:", self.current_account.balance)

    def add_income(self):
        income = self.__input_int("Enter income:\n")
        self.current_account.balance += income
        if DB_Storage.db_change_balance(self.db_connect, self.current_account):
            print("Income was added!")
        else:
            print("Income wasn't added! Error occur.")


    def do_trasfer(self):
        print("Transfer")
        card_num = str(BankingService.__input_int("Enter card number:\n"))

        '''stupid check - make more accurate
        if card_num[:6] != str(self.card_generator.IIN):
            print("Such a card does not exists.")
            return
        '''
        if not self.card_generator.check_checksum(card_num):
            print("Probably you make a mistake in card number. Please try again!")
            return

        transferTo = DB_Storage.db_check_card(self.db_connect, card_num)

        if transferTo is None:
            print("Such a card does not exists.")
            return

        amount_money = self.__input_int("Enter how much money you want to transfer:\n")
        if self.current_account.balance < amount_money:
            print("Not enough money!")
            return
        else:
            transferTo = list(transferTo)
            self.current_account.balance -= amount_money
            DB_Storage.db_change_balance(self.db_connect, self.current_account)
            transferTo[1] += amount_money
            DB_Storage.db_change_balance(self.db_connect, credit_card.CreditCard(transferTo[0], 0, transferTo[1]))
            print("Success!")

    def close_account(self):
        if DB_Storage.db_delete_account(self.db_connect, self.current_account):
            print("The account has been closed!")

    def exit(self):
        self.working_session = False

    def menu_output(self):
        for item, name in self.current_menu.items():
            print(str(item) + ".", name[0])

    def handle_menu(self):
        while self.working_session:

            if self.db_connect is None:
                self.exit()

            self.menu_output()
            try:
                usr_inp = BankingService.__input_int()
                self.current_menu[usr_inp][1]()
            except KeyError:
                print("Something goes wrong try again")
        print("Bye!")


if __name__ == "__main__":
    bankService = BankingService()
    bankService.handle_menu()

