import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from "react-router-dom"

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route>
            <Route path="/" element={<Home />} />
            <Route path="/about" element={<About />} />
            <Route path="/contact" element={<Contact />} />
        </Route>
    )
)

function App() {
    return <RouterProvider router={router} />
}