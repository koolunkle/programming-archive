import {
    render
} from "@testing-library/react";
import { describe } from 'node:test';
import { Counter } from "./Counter";

/*
    목 API 서비스

    Jest의 mock() 함수를 사용해서 기존 updateCount() API 서비스 메소드를 모의 구현함으로써,
    컴포넌트가 해당 메소드를 호출했을 때의 응답을 시뮬레이션한다
    
    테스트의 나머지 부분에서는 사용자 증가 버튼 클릭을 시뮬레이션하고, 모의 구현한 API 응답을 기다리고,
    이후 컴포넌트가 새로운 데이터를 반영해 UI를 업데이트했는지 확인한다
*/
jest.mock("./apiService", () => ({
    updateCount: jest.fn()
}))

describe("Counter", () => {
    test("UI updates on item count++", async () => {
        const mockData = {
            id: 1,
            name: "Item 1",
            count: 2
        }

        const { queryByText } = render(
            <Counter
                item={{
                    id: 1,
                    name: "Item 1",
                    count: 0
                }} />
        )

        // 증가 버튼을 클릭
        fireEvent.click(queryByText("+"))

        // API 응답을 기다린다
        await waitFor(() => {
            expect(
                require("./apiService").updateCount,
            ).toHaveBeanCalledWith(1, mockData.count)
        })

        // 새로운 데이터를 반영한 UI 업데이트를 확인한다
        expect(
            queryByText("Count: 2").not().toBeNull()
        )
    })
})