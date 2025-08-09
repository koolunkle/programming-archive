import db from "./database";
import { Comments } from "./subscribe-promise-with-use";

async function BlogPost({ postId }) {
    // ...

    // 서버 액션
    async function upvoteAction(commentId) {
        "user server";
        await db.comments.incrementVotes(commentId, 1);
    }

    return (
        <div>
            {/*  */}

            {/* 서버 액션은 props로서 아래로 전달된다 */}
            <Comments
                commentsPromise={comments}
                upvoteAction={upvoteAction}
            />
        </div>
    )
}
