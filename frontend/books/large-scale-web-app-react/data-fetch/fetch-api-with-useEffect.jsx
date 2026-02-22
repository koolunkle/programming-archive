const { useEffect } = require("react")

const App = () => {
    // ...

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await fetch(
                    "https://api.example.com/data",
                );
                if (!response.ok) {
                    throw new Error(
                        "Network response was not ok",
                    );
                }
                const data = await response.json();
                console.log(data);
            } catch (error) {
                console.error(
                    "There was a problem:",
                    error.message,
                );
            }
        }
        fetchData();
    }, []);
    // ...
};