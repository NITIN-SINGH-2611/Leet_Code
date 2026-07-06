import { apiClient } from "@/lib/apiClient";

export interface AuthUser {
  id: number;
  email: string;
  fullName: string;
  role: string;
  xp: number;
  level: number;
  currentStreak: number;
  longestStreak: number;
  targetQuestionsPerDay: number;
}

interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: AuthUser;
}

interface ApiEnvelope<T> {
  success: boolean;
  message?: string;
  data: T;
}

const ACCESS_TOKEN_KEY = "cc_access_token";
const REFRESH_TOKEN_KEY = "cc_refresh_token";

export function storeTokens(accessToken: string, refreshToken: string) {
  localStorage.setItem(ACCESS_TOKEN_KEY, accessToken);
  localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
}

export function clearTokens() {
  localStorage.removeItem(ACCESS_TOKEN_KEY);
  localStorage.removeItem(REFRESH_TOKEN_KEY);
}

export function getRefreshToken() {
  return localStorage.getItem(REFRESH_TOKEN_KEY);
}

export async function signup(fullName: string, email: string, password: string): Promise<AuthUser> {
  const { data } = await apiClient.post<ApiEnvelope<AuthResponse>>("/auth/signup", {
    fullName,
    email,
    password,
  });
  storeTokens(data.data.accessToken, data.data.refreshToken);
  return data.data.user;
}

export async function login(email: string, password: string): Promise<AuthUser> {
  const { data } = await apiClient.post<ApiEnvelope<AuthResponse>>("/auth/login", {
    email,
    password,
  });
  storeTokens(data.data.accessToken, data.data.refreshToken);
  return data.data.user;
}

export async function fetchMe(): Promise<AuthUser> {
  const { data } = await apiClient.get<ApiEnvelope<AuthUser>>("/auth/me");
  return data.data;
}

export async function forgotPassword(email: string): Promise<void> {
  await apiClient.post("/auth/forgot-password", { email });
}

export async function resetPassword(token: string, newPassword: string): Promise<void> {
  await apiClient.post("/auth/reset-password", { token, newPassword });
}

export function logout() {
  clearTokens();
}
