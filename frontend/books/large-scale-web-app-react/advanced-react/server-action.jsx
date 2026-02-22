"use server"
import db from "./database";

// 별도 파일에 정의된 서버 액션
export async function upvoteAction(commentId) {
    await db.comments.incrementVote(commentId, 1);
}

export async function downvoteAction(commentId) {
    await db.comments.incrementVotes(commentId, -1);
}