/**
 * [정적 임포트]
 *  표준적인 정적 임포트(static import)를 사용하는 경우 파일 맨 위에서 import를 선언해 컴포넌트를 임포트한다
 *   
 *  만약 Post 컴포넌트의 크기가 커서 로딩하는데 시간이 걸리는 경우, 특히 초기 렌더링 시 Post 컴포넌트가 즉시 필요하지 않다면 
 *  상위 컴포넌트인 App 컴포넌트의 초기 로딩 시간에 영향을 줄 수 있다
 * 
 *  정적 임포트는 전체 컴포넌트와 그 의존성들을 가져와서 실행한 후 App 컴포넌트를 초기화하기 때문이다
 * 
 * [동적 임포트]
 *  정적 임포트 시의 이슈를 피하기 위해 리액트의 lazy() 함수 안에서 
 *  컴포넌트를 지연해서 로딩할 수 있는데 이를 동적 임포트(dynamic import)라고 한다
 * 
 *  동적 import() 구문을 활용해 컴포넌트를 비동기로 로딩한다
 *  import() 함수는 프로미스를 반환하고, 이는 임포트된 모듈로 해결된다
 *  이를 리액트의 lazy() 함수와 함께 활용하면 지연 로딩되는 컴포넌트를 만들 수 있다
 * 
 *  컴포넌트는 실제로 필요할 때만 로딩되므로 초기 번들 크기를 줄이고 App 컴포넌트의 로딩 시간을 개선한다
 *  Suspense 컴포넌트는 컴포넌트가 로딩되는 동안 로딩 메시지 또는 플레이스홀더(placeholder)를 표시하기 위해 사용된다
 */

import { Suspense, useRef, useState } from 'react';
import Post from './post/Posts';

// [Post 컴포넌트 임포트]
// function App() {
//     return (
//         <div>
//             <Post />
//         </div>
//     )
// }

// Post 컴포넌트 동적 임포트
// const Post = lazy(
//     () => import("./components/Post")
// )

// function App() {
//     return (
//         <div>
//             {/* Suspense를 사용해 Post가 동적으로 로딩되는 동안 대체 컴포넌트를 렌더링한다 */}
//             <Suspense fallback={<div>Loading...</div>}>
//                 <Post />
//             </Suspense>
//         </div>
//     )
// }

/**
 * [상호작용에 대한 지연 로딩]
 *  지연 로딩 패턴은 상호작용(interaction)이나 클릭이 발생할 때, 컴포넌트를 동적으로 로딩할 때도 사용할 수 있다
 *  다음은 Post 컴포넌트를 지연 로딩하는 예시로써 부모가 렌더링될 때 해당 컴포넌트를 동적으로 로딩하는 대신 클릭했을 때 로딩한다
 * 
 *  리액트의 lazy() 함수를 동적 임포트와 함께 사용하는 것은 default exports를 사용할 때만 효과가 있다
 *  따라서 Post 컴포넌트가 이름 있는 export를 갖는다면 그에 따라 import 구문을 조정해야 한다
 * 
 *  또한 lazy() 함수를 사용한 동적 임포트는 즉시 필요하지 않은 큰 컴포넌트에만 사용해야 한다
 *  코드에 약간의 복잡성이 추가되고 서버 사이드 렌더링과 관련된 이슈를 야기할 수 있기 때문이다
 */

// function App() {
//     cosnt[Post, setPost] = useState(null)

//     const handleClick = () => {
//         import("./components/Post").then((module) => {
//             setPost(() => module.default)
//         })
//     }

//     return (
//         <div>
//             {
//                 Post
//                     ? (<Post />)
//                     : (<button onClick={handleClick}>Load Post</button>)
//             }
//         </div>
//     )
// }

// Intersection Observer를 사용한 지연 로딩
const Post = lazy(
    () => import("./post/Posts")
)

function App() {
    const [shouldRenderPost, setShouldRenderPost] = useState(false)
    const postRef = useRef(null)

    const handleIntersect = ([entry]) => {
        if (entry.isIntersecting) {
            setShouldRenderPost(true)
        }
    }

    /**
     *  useIntersectoinObserver 훅을 사용해서 postRef 요소의 가시성의 변화를 주시하고
     *  해당 요소가 뷰포트에 들어왔을 때 handleIntersect() 콜백을 트리거한다
     * 
     *  shouldRenderPost 상태 속성은 해당 요소가 만나면 true 값으로 설정되고
     *  이는 Suspense 컴포넌트 안에 있는 Post 컴포넌트의 렌더링을 트리거한다
     *   - 해당 훅은 코드베이스 어딘가에 정의되어 있다고 가정
     */ 
    useIntersectoinObserver(
        postRef,
        handleIntersect,
        { threshold: 0 }
    )

    return (
        <div>
            <div style={{ height: "1000px" }}>
                Some content before the post
            </div>
            <div ref={postRef}>
                {shouldRenderPost
                    ? (<Suspense fallback={<div>Loading...</div>}><Post /></Suspense>)
                    : (<div>Loading...</div>)
                }
            </div>
            <div style={{ height: "1000px" }}>
                Some content after the post
            </div>
        </div>
    )
}

export default App;