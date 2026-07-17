from flask import Flask, render_template, request
from config import Config
import requests

app = Flask(__name__)
app.config.from_object(Config)


@app.route("/", methods=["GET", "POST"])
def home():

    weather = None
    error = None

    if request.method == "POST":

        city = request.form.get("city")

        url = (
            f"https://api.openweathermap.org/data/2.5/weather"
            f"?q={city}&appid={app.config['API_KEY']}&units=metric"
        )

        response = requests.get(url)
        data = response.json()

        if data.get("cod") == 200:

            weather = {
                "city": data["name"],
                "country": data["sys"]["country"],
                "temp": data["main"]["temp"],
                "feels_like": data["main"]["feels_like"],
                "humidity": data["main"]["humidity"],
                "description": data["weather"][0]["description"],
                "wind": data["wind"]["speed"]
            }

        else:
            error = "City not found."

    return render_template(
        "index.html",
        weather=weather,
        error=error
    )


if __name__ == "__main__":
    app.run(debug=True)