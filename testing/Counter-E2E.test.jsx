// 인덱스(/) 라우트 테스트하기
describe('/', () => {
    // beforeEach 훅은 describe 블록의 각 테스트가 시작되기 전에 실행된다
    // Cypress 명령어인 cy.visit('/')는 브라우저에게 웹 애플리케이션의 인덱스 라우트(/)를 탐색하게 한다
    // 이 설정은 스위트의 모든 테스트가 인덱스 라우트에서 시작되는 것을 보장한다
    beforeEach(() => {
        cy.visit('/')
    })

    it('redirects to /counter on link click', () => {
        // 테스트 함수에서 cy.get() 함수를 사용해서 해당 함수가 페이지에서 찾아낸 첫 번째 앵커 태그를 선택
        // 이 앵커 태그는 'Go to Counter' 링크일 것이라 가정, click()은 선택된 요소에 대해 클릭 이벤트를 시뮬레이션하는 명령어이다
        cy.get('a').click()
        // cy.url()를 사용해 브라우저의 현재 URL을 추출하고 해당 URL이 문자열 /counter를 포함하고 있는지 확인한다
        // 이것은 페이지의 링크를 클릭한 뒤 애플리케이션이 의도한 대로 /counter 라우트를 탐색했는지 확인한다
        cy.url().should('include', '/counter')
    })
})

/*
    /counter 라우트 테스트하기

    /counter 라우트를 테스트하기 위해 API로부터 데이터 가져오기, count 값 증가 및 감소하기, 변경 사항을 서버에 저장하기,
    업데이트 내용을 클라이언트와 동기화하기를 테스트할 것이다. /counter 라우트에 먼저 방문한 뒤 모든 테스트가 실행되도록 설정한다

    /counter 라우트에 표시된 컴포넌트는 API와 상호작용해서 서버로부터 데이터를 가져오거나 사용자가 적절한 버튼을 클릭했을 때 해당 데이터를 업데이트한다
    Cypress에서 테스트를 실행할 때 테스트가 실제로 서버에 연결되는지 확인하거나 이 요청을 '스텁' 혹은 '목'으로 만들 수 있다
*/
describe('/counter', () => {
    beforeEach(() => {
        cy.visit('/counter')
    })
    // ...
})

// /counter에 대한 GET 요청을 스텁으로 만들기
describe('/counter', () => {
    beforeEach(() => {
        // cy.intercept()를 사용해서 /api/items 엔드포인트에 대해 만들어진 GET 요청을 가로챈 뒤, 응답으로써 미리 정해진 배열을 반환하게 함
        // 이와 같은 방식으로 테스트를 하는 동안 counter 컴포넌트에 표시되는 데이터를 통제한다
        cy.intercept('GET', '/api/items', [
            { id: 1, name: 'Item 1', count: 0 },
            { id: 2, name: 'Item 2', count: 0 },
        ])
        cy.visit('/counter')
    })
})

/*
    /counter 라우트에서의 데이터 가져오기 및 표시 테스트하기

    beforeEach 훅을 설정한 상태에서 /counter 라우트에 대한 몇 가지 테스트를 작성할 수 있다
        - counter 리스트는 올바르게 데이터를 가져오고 표시한다
        - 아이템 리스트를 가져온 뒤, 올바른 버튼을 클릭했을 때 각 아이템의 counte 값을 증가/감소시킬 수 있다
    
    아래 예제는 먼저 가져온 데이터가 페이지에 올바르게 표시되는지 확인하는 테스트를 작성한다
    cy.get() 유틸리티를 사용해서 count 클래스의 모든 요소를 선택한 뒤(이들은 표시된 각 counter의 count 값과 일치해야 한다)
    이 중 2개의 요소가 존재하고 이들의 텍스트 콘텐츠가 기대한 결과와 이리하는지 확인한다    
*/
describe('/counter', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/items', [
            { id: 1, name: 'Item 1', count: 0 },
            { id: 2, name: 'Item 2', count: 0 },
        ])
        cy.visit('/counter')
    })

    it('displays counters fetched from API', () => {
        cy.get('.count').should(($counts) => {
            expect($counts).to.have.length(2)
            expect($counts.eq(0)).to.contain.text('0')
            expect($counts.eq(1)).to.contain.text('0')
        })
    })
})

/*
    /counter 라우트에서 증가 기능 테스트하기

    cy.get()을 사용해서 increment 클래스의 모든 요소를 선택한 뒤 first()를 호출해 컬렉션의 첫 번째 요소를 특정한다
*/
describe('/counter', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/items', [
            { id: 1, name: 'Item 1', count: 0 },
            { id: 2, name: 'Item 2', count: 0 },
        ])
        cy.visit('/counter')
    })

    it('displays counters fetched from API', () => {
        cy.get('.count').should(($counts) => {
            expect($counts).to.have.length(2)
            expect($counts.eq(0)).to.contain.text('0')
            expect($counts.eq(1)).to.contain.text('0')
        })
    })

    it('increases count on increment click', () => {
        const updatedItem = {
            id: 1,
            name: "Item 1",
            count: 2,
        }

        // 첫 번째 증가 버튼을 두 번 클릭한 동작 시뮬레이션
        cy.get(".increment").first().click().click()

        cy.get(".count").first().should("contain.text", updatedItem.count)
    })
})

// /counter 라우트에서 감소 기능 테스트하기
describe('/counter', () => {
    beforeEach(() => {
        cy.intercept('GET', '/api/items', [
            { id: 1, name: 'Item 1', count: 0 },
            { id: 2, name: 'Item 2', count: 0 },
        ])
        cy.visit('/counter')
    })

    it('displays counters fetched from API', () => {
        cy.get('.count').should(($counts) => {
            expect($counts).to.have.length(2)
            expect($counts.eq(0)).to.contain.text('0')
            expect($counts.eq(1)).to.contain.text('0')
        })
    })

    it('increases count on increment click', () => {
        const updatedItem = {
            id: 1,
            name: "Item 1",
            count: 2,
        }

        // 첫 번째 증가 버튼을 두 번 클릭한 동작 시뮬레이션
        cy.get(".increment").first().click().click()

        cy.get(".count")
            .first()
            .should("contain.text", updatedItem.count)
    })

    it('decreases count on decrement click', () => {
        const updatedItem = {
            id: 2,
            name: "Item 2",
            count: -2,
        }

        cy.get(".decrement").first().click().click()

        cy.get(".count")
            .first()
            .should("contain.text", updatedItem.count)
    })
})