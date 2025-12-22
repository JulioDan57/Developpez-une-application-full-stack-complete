export interface Article {
  articleId: number;
  title: string;
  content: string;
  createdAt: string;
  author: Author;
  subject: Subject;
  comments: Comment[];
}

export interface Author {
  userId: number;
  email: string;
  username: string;
}

export interface Subject {
  subjectId: number;
  name: string;
  description: string;
  subscribed: boolean;
}

export interface Comment {
  commentId: number;
  content: string;
  createdAt: string;
  author: string;
}

export interface ArticleListResponse {
  articles: Article[];
}