import PostContent from "./components/PostContent";
import PostFooter from "./components/PostFooter";
import PostHeader from "./components/PostHeader";

/**
 * [한 컴포넌트에 모든 기능을 포함하고 있는 하나의 포스트 요소]
 *  아래 Post 컴포넌트는 소셜 네트워크 사이트에서 어떤 저자에 의해 생성된 포스트를 위한 기능을 포함한다
 *  해당 포스트는 저자의 이름, 포스트 제목, 본문, 날짜 및 다른 세부 사항들을 포함한다
 *  하나의 단일 컴포넌트 안에 포스트 요소의 모든 콘텐츠를 렌더링하였다
 *  post prop은 컴포넌트에 매개변수로 전달되고 이를 사용해 제목, 본문, 저자, 날짜 및 다른 관련 정보들을 표시한다
 * 
 *  특정 요소의 모든 UI를 하나의 컴포넌트 안에 유지하는 접근법을 사용하면 단순하고 빠르게 구현할 수 있다
 *  하지만 코드베이스가 커지면 컴포넌트의 유지보수와 테스트가 어려워진다
 *  모듈성과 컴포넌트화를 사용하면 유지보수와 테스트에 도움을 줄 수 있으며
 *  동시에 컴포넌트의 재사용 가능성을 높이고 보다 유연하게 만들 수 있다 
 */

function Post({ post }) {
    return (
        <div>
            <img
                src={post.profileUrl}
                alt={`${post.author}'s profile`} />

            <h1>{post.title}</h1>

            <p>{post.text}</p>

            <div>Author: {post.author}</div>

            <div>Date: {post.date}</div>

            <p>{`${post.numLikes} likes`}</p>

            <p>{`${post.numComments} comments`}</p>

            <p>{`${post.numShares} shares`}</p>

            <button>Like</button>

            <button>Share</button>

            <button>Comment</button>
        </div>
    )
}

export default Post;

/**
 * [애플리케이션을 작은 컴포넌트로 나누라]
 * 컴포넌트화의 핵심적인 측면은 애플리케이션을 보다 작고, 관리 가능한 컴포넌트들로 자르는 것이다
 * 거대한 모놀리식 컴포넌트들을 만드는 대신 애플리케이션을 개발, 테스트, 유지보수하기 쉬운 보다 작은 컴포넌트로 나눌 수 있다
 * 작은 컴포넌트들은 보다 유연하며 여러 콘텍스트에서 재사용할 수 있으며 애플리케이션의 적응성과 확장성을 높여준다
 * Post 컴포넌트의 경우 다음의 작은 컴포넌트로 나눌 수 있다
 *  - PostHeader
 *  - PostContent
 *  - PostFooter
 * 
 *  모든 것을 컴포넌트로 만들고 싶을 수도 있겠지만 균형이 중요하다
 *  너무 잘게 나누면 결국 그 코드베이스를 탐색하기 어렵게 될 것이고, 너무 성기게 나누면 컴포넌트화의 혜택을 누리지 못한다
 *  컴포넌트로 나누는 것을 결정할 때는 여러 요소들을 고려해야 하는데 내용은 다음과 같다
 *   - 재사용성: 특정한 UI 요소 혹은 기능을 애플리케이션의 여러 부분에서 반복하기 원할 때 컴포넌트화 할 수 있다
 *   - 단순성과 가독성: 컴포넌트를 작은 하위 컴포넌트로 나누고 각각의 책임에 집중하도록 만들면 코드를 보다 쉽게 읽고 이해할 수 있다
 *   - 개선된 테스트 가능성: 작은 컴포넌트들은 일반적으로 적은 내부 상태와 적은 사이드 이펙트를 가지므로 테스트에서 격리하기 쉽고 기능의 각 조각이 의도한 대로 작동함을 보장한다
 *   - 성능 고려 사항: 작은 컴포넌트가 보다 적은 수의 재렌더링을 하며 컴포넌트와 계산을 메모화한다. 이때 앱의 전반적인 성능을 개선할 수 있다
 */   

function Posts({
    author,
    content,
    footer
}) {
    return (
        <div>
            <PostHeader
                authorName={author.name}
                profileUrl={author.profileUrl}
                timestamp={author.timestamp}
            />

            <PostContent
                text={content.text}
                media={content.media}
            />

            <PostFooter
                numLikes={footer.numLikes}
                numComments={footer.numComments}
                numShares={footer.numShares}
            />
        </div>
    )
}