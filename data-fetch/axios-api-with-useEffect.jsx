const { useEffect } = require("react");

const App = () => {
    // ...

    useEffect(() => {
        async function fetchData() {
            try {
                const response = await axios.get(
                    "https://api.example.com/data",
                );
                console.log(response.data);
            } catch (error) {
                console.error(
                    "There was a problem",
                    error.message,
                );
            }
        };
        fetchData();
    }, []);
    // ...
};