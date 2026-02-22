// use()를 사용해 프로미스 구독하기

// 리액트 클라이언트 컴포넌트
export function Comments({
    commentsPromise,
    upvoteAction
}) {
    const comments = use(commentsPromise);

    // Comments 클라이언트 컴포넌트에서 사용된 서버 액션
    return (
        <ul>
            {comments.map((comment) => {
                <li key={comment.id}>
                    {/* <Comment {...comment} /> */}

                    {comment.text} (Votes: {comment.votes})
                    <button onClick={upvoteAction}>
                        Upvote
                    </button>
                </li>
            })}
        </ul>
    )
}