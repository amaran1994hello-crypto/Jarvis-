class Notifier:
    @staticmethod
    def send_push(title: str, body: str):
        print(f"[PUSH] {title}: {body}")
        return True
