/**
 * [PostFooter]
 *  PostFooter 컴포넌트는 포스트의 푸터를 출력하는 책임을 갖는다
 *  post의 좋아요, 댓글, 공유 수를 위한 props를 받는다
 */

function PostFooter({
    numLikes,
    numComments,
    numShares
}) {
    return (
        <div>
            <p>{`${numLikes} likes`}</p>
            <p>{`${numComments} comments`}</p>
            <p>{`${numShares} shares`}</p>

            <button>Like</button>
            <button>Share</button>
            <button>Comment</button>
        </div>
    )
}

export default PostFooter;