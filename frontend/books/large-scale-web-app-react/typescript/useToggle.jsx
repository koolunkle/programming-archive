// 자바스크립트로 작성한 커스텀 useToggle 훅 

const { useState, useCallback } = require("react")

const useToggle = (initialState = false) => {
    const [isOn, setIsOn] = useState(initialState)

    const toggle = useCallback(() => {
        setIsOn(prevIsOn => !prevIsOn)
    }, [])

    return [isOn, toggle]
}