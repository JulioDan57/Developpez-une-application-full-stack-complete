export interface RegisterRequest {
  email: string;
  username: string;
  password: string;
}

export interface LoginRequest {
  usernameOrEmail: string;
  password: string;
}

export interface AuthSuccessResponse {
  token: string;
}

export interface ApiErrorResponse {
  status: number;
  message: string;
  errors: Record<string, string> | null;
  timestamp: string;
}

export interface UserProfileResponse {
  userId: number;
  email: string;
  username: string;
  subscriptions: UserSubscription[];
}

export interface UserSubscription {
  subscriptionId: number;
  subjectId: number;
  subjectName: string;
  subjectDescription: string;
}