import random
import DB_Storage

class CreditCardManager:
    IIN = 400000
    accounts_number = set()  # store here set of

    def __init__(self, db_connect):
        random.seed()
        self.account_identifier = 0
        self.checksum = 0
        self.pincode = 0
        if db_connect is not None:
            res = DB_Storage.db_get_accounts(db_connect)
            if res is not None:
                for num in res:
                    CreditCardManager.accounts_number.add(num)

    def get_credit_card_number(self):
        return str(CreditCardManager.IIN) + str(self.account_identifier) + str(self.checksum)

    def generate(self):

        while True:
            self.account_identifier = random.randint(100000000, 999999999)  #int(random.random() * 1000000000)
            if self.account_identifier not in CreditCardManager.accounts_number:
                CreditCardManager.accounts_number.add(self.account_identifier)
                numbers = [int(val) for val in self.get_credit_card_number()]
                numbers = [val * 2 if ind % 2 == 0 else val for val, ind in zip(numbers, range(0, 16))]
                sum_nums = sum([val - 9 if val > 9 else val for val in numbers])
                self.checksum = 10 - sum_nums % 10
                break
        self.pincode = random.randint(0, 9999)
        return self.get_credit_card_number(), str(self.pincode)



def check_Luhn(number):
    numbers = [int(val) for val in number[:-1]]
    print("Numbers:", numbers)
    numbers = [val * 2 if ind % 2 == 0 else val for val, ind in zip(numbers, range(0, 16))]
    print("Mult*2 :", numbers)
    numbers = [val - 9 if val > 9 else val for val in numbers]
    print("sub9   :", numbers)
    sum_nums = sum(numbers)
    print("Summ:", sum_nums)
    checksum = 10 - sum_nums % 10
    print("Check summ:", checksum)
    return checksum == number[-1]

def generate():
    while True:
        account_identifier = 7260233680
        acc_ident_str = str(400000) + str(account_identifier)
        numbers = [int(val) for val in acc_ident_str]
        numbers = [val * 2 if ind % 2 == 0 else val for val, ind in zip(numbers, range(0, 16))]
        sum_nums = sum([val - 9 if val > 9 else val for val in numbers])
        checksum = 10 - sum_nums % 10
        break
    return acc_ident_str[:-1] + str(checksum)


class CreditCard:
    def __init__(self, card_number, pin_code, balance=0):
        self.card_num = card_number
        self.pin_code = pin_code
        self.balance = balance

    def set_balance(self, balance):
        self.balance = balance

    def __eq__(self, other):
        return self.card_num == other.card_num

    def __hash__(self):
        return hash(self.card_num)


def create_credit_card(card_gen):
    return CreditCard(*card_gen.generate())

#def create_credit_card():
#    return CreditCard(*cardGenerator.generate())

if __name__ == "__main__":
    num = generate()
    print("generate:", num)
    print("Check", check_Luhn(num))
