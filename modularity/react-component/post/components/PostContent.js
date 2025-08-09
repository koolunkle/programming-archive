/**
 * [PostContent]
 *  PostContent 컴포넌트는 포스트의 메인 콘텐츠를 출력하는 책임을 갖는다
 *  PostContent는 포스트의 본문, 그리고 필요한 경우 미디어 요소(이미지, 비디오)를 위한 props를 받는다
 */

function PostContent({ text, media }) {
    return (
        <div>
            <p>{text}</p>
            {media.map((element, index) => {
                <img
                    key={index}
                    src={element.url}
                    alt={element.alt}
                />
            })}
        </div>
    )
}

export default PostContent;