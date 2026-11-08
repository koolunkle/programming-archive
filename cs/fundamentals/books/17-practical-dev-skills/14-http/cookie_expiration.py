import uuid

from flask import Flask, make_response, request

app = Flask(__name__)


@app.route("/")
def hello_world():
    cookies = request.cookies

    if "sessionId" in cookies:
        response = make_response(
            "기존 연결입니다: sessionId={}".format(cookies["sessionId"])
        )
    else:
        new_session_id = str(uuid.uuid4())
        response = make_response(f"새 연결입니다: sessionId={new_session_id}")
        response.set_cookie("sessionId", new_session_id, max_age=5)

    return response


app.run()
