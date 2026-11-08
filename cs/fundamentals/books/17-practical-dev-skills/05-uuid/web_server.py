import uuid

from flask import Flask

app = Flask(__name__)


@app.route("/")
def hello():
    request_id = uuid.uuid4()
    print(f"API 요청 ID={request_id}")
    return "Hello World"


if __name__ == "__main__":
    app.run()
