import random


class CreditCardGenerator:
    IIN = 400000
    account_identifiers = []

    def __init__(self):
        random.seed()
        self.account_identifier = 0
        self.checksum = 0
        self.pincode = 0

    def get_credit_card_number(self):
        return str(CreditCardGenerator.IIN) + str(self.account_identifier) + str(self.checksum)

    def generate(self):
        self.checksum = random.randint(0, 9)
        while True:
            self.account_identifier = random.randint(100000000,999999999)  #int(random.random() * 1000000000)
            if self.account_identifier not in CreditCardGenerator.account_identifiers:
                CreditCardGenerator.account_identifiers.append(self.account_identifier)
                break
        self.pincode = random.randint(0, 9999)
        return self.get_credit_card_number(), str(self.pincode)



class CreditCard:

    def __init__(self, card_number, pin_code):
        self.card_num = card_number
        self.pin_code = pin_code
        self.balance = 0

    def set_balance(self, balance):
        self.balance = balance


cardGenerator = CreditCardGenerator()


def create_credit_card():
    return CreditCard(*cardGenerator.generate())
