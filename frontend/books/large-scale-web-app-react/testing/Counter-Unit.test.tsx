import { render } from '@testing-library/react';
import { describe, it } from 'node:test';
import Counter from './Conter';

describe('Counter component', () => {
    it('initial count is 0', () => {
        // ...
    })

    it('increases count on "+" button click', () => {
        // ...
    })

    it('decreases count on "-" button click', () => {
        // ...
    })
})

// count 초깃값이 0인지 테스트하기
// queryByText() 유틸리티 메소드를 사용해서 count 값을 표시하는 span 요소를 찾아 초깃값이 0인지 확인한다.
// 확인을 위해 Jest의 expect() 헬퍼를 사용할 수 있다. 해당 요소를 찾고 null이 아닌지(즉, 요소가 값을 갖는지) 확인한다
describe('Counter component', () => {
    it('renders with initial count of 0', () => {
        const { getByText } = render(<Counter />)
        expect(getByText('0')).not.toBeNull()
    })
    // ...
})

// count 증가 및 감소에 관한 테스팅
describe('Counter component', () => {
    it('renders with initial count of 0', () => {
        const { getByText } = render(<Counter />)
        expect(getByText('0')).not.toBeNull()
    })

    it('increases count on "+" button click', () => {
        const { getByText } = render(<Counter />)
        const incrementButton = getByText('+')

        fireEvent.click(incrementButton)
        expect(getByText('1')).not.toBeNull()
    })

    it('decreases count on "-" button click', () => {
        const { getByText } = render(<Counter />)
        const decrementButton = getByText('+')

        fireEvent.click(decrementButton)
        expect(getByText('-1')).not.toBeNull()
    })
})

// 정리, 동작, 확인 패턴을 Counter 컴포넌트 테스트에 적용하기
describe('Counter component', () => {
    it('renders with initial count of 0', () => {
        // 정리
        const { getByText } = render(<Counter />)

        // 동작 - 초기 렌더링을 테스트하므로 아무런 동작이 필요하지 않다

        // 확인
        expect(getByText('0')).not.toBeNull()
    })

    it('increases count on "+" button click', () => {
        // 정리
        const { getByText } = render(<Counter />)
        const incrementButton = getByText('+')

        // 동작
        fireEvent.click(incrementButton)

        // 확인
        expect(getByText('1')).not.toBeNull()
    })

    it('decreases count on "-" button click', () => {
        // 정리
        const { getByText } = render(<Counter />)
        const decrementButton = getByText('+')

        // 동작
        fireEvent.click(decrementButton)

        // 확인
        expect(getByText('-1')).not.toBeNull()
    })
})