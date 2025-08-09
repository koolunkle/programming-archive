/*
    리액트 서버 컴포넌트

    해당 컴포넌트는 제공된 postId를 사용해 데이터베이스에서 블로그 포스트 데이터를 비동기로 가져온다
    포스트에 대한 데이터(title, summarym author, content 등)는 이후 div 요소 안에서 렌더링된다
*/

import { Comments } from "./comment";
import db from "./database";

// 리액트 서버 컴포넌트
async function BlogPost({ postId }) {
    // 데이터베이스에서 블로그 포스트 데이터를 로딩한다
    const post = await db.posts.get(postId);

    // 데이터베이스에서 포스트에 대한 댓글을 로딩한다
    const comments = await db.comments.getByPostId(postId);

    return (
        <div>
            <h2>{post.title}</h2>
            <p>{post.summary}</p>
            <p>Written by {post.author}</p>
            <p>{post.content}</p>

            {/* Comment 클라이언트 컴포넌트 목록을 렌더링한다 */}

            {/* <ul>
                {comments.map((comment) => (
                    <li key={comment.id}>
                        <Comment {...comment} />
                    </li>
                ))}
            </ul> */}

            <Suspense fallback={<p>Loading comments</p>}>
                <Comments commentsPromise={comments} />
            </Suspense>
        </div>
    )
}