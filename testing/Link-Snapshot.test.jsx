import renderer from "react-test-renderer";
import Link from "./Link";

it("renders correctly", () => {
    const tree = renderer
        .create(
            <Link page="http://www.facebook.com">
                Facebook
            </Link>
        )
        .toJson()
    expect(tree).toMatchSnapshot()
})

/*
    Link 컴포넌트의 초기 스냅숏
      
    exports[`renders correctly 1`] = `
      <a
        href="http://www.facebook.com"
      >
        Facebook
      </a>    
      `
*/

/*
    다른 링크를 렌더링하도록 테스트를 업데이트하기

    컴포넌트 출력을 변경하고 싶다고 가정, 컴포넌트가 테스트 시 다른 링크를 렌더링하도록 설정해 출력을 변경할 수 있다
    아래 변경을 적용한 뒤 테스트를 실행하면 Jest는 새로운 출력과 초기 스냅숏 사이의 차이를 지적한다
    이를 통해 컴포넌트의 동작에 의도하지 않은 변경이 발생했음을 알 수 있다

    테스트가 실패하고, Jest가 업데이트된 출력과 저장된 스냅숏의 불일치를 식별했음을 알 수 있으며,
    이 변경이 의도된 것이라면 다음과 같이 스냅숏을 간단하게 업데이트할 수 있다

    - Jest 스냅숏 업데이트하기: jest --updateSnapshot

    스냅숏 참조가 업데이트되면 테스트는 성공한다
*/
it('renders correctly', () => {
    const tree = renderer
        .create(
            <Link page="http://www.instagram.com">
                Instagram
            </Link>
        )
        .toJSON()
    expect(tree).toMatchSnapshot()
})