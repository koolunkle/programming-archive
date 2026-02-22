/**
 * [PostHeader]
 *  PostHeader 컴포넌트는 포스트의 헤더를 출력하는 책임을 갖는다
 *  저자의 이름, 프로파일 이미지, 타임 스탬프를 위한 props를 받는다
 */

function PostHeader({
    authorName,
    profileUrl,
    timestamp
}) {
    return (
        <div>
            <img
                src={profileUrl}
                alt={`${authorName}'s profile`} />
            <p>{authorName}</p>
            <p>{timestamp}</p>
        </div>
    )
}

export default PostHeader;