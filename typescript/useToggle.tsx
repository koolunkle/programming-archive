import { useCallback, useState } from 'react';

type UseToggle = {
    isOn: Boolean,
    toggle: () => void;
}

const useToggle = (initialState: boolean): [boolean, () => void] => {
    const [isOn, setIsOn] = useState(initialState)

    const toggle = useCallback(() => {
        setIsOn(prevIsOn => !prevIsOn)
    }, [])

    return [isOn, toggle] as const;
}

export default useToggle;