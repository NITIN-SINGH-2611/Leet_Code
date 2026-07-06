import { apiClient } from "@/lib/apiClient";
import type {
  Analytics,
  ApiResponse,
  DashboardSummary,
  PageResponse,
  ProgressStatus,
  QuestionDetail,
  QuestionFilters,
  QuestionSummary,
  StudyPlan,
  Topic,
} from "@/types";

export async function fetchTopics(): Promise<Topic[]> {
  const { data } = await apiClient.get<ApiResponse<Topic[]>>("/topics");
  return data.data;
}

export async function fetchTopicBySlug(slug: string): Promise<Topic> {
  const { data } = await apiClient.get<ApiResponse<Topic>>(`/topics/${slug}`);
  return data.data;
}

export async function fetchQuestions(
  filters: QuestionFilters = {}
): Promise<PageResponse<QuestionSummary>> {
  const { data } = await apiClient.get<ApiResponse<PageResponse<QuestionSummary>>>("/questions", {
    params: filters,
  });
  return data.data;
}

export async function fetchQuestionBySlug(slug: string): Promise<QuestionDetail> {
  const { data } = await apiClient.get<ApiResponse<QuestionDetail>>(`/questions/${slug}`);
  return data.data;
}

interface ProgressView {
  questionId: number;
  status: ProgressStatus;
  attempts: number;
  confidenceRating: number | null;
  bookmarked: boolean;
  personalNotes: string | null;
}

export async function updateProgress(
  questionId: number,
  update: Partial<Pick<ProgressView, "status" | "confidenceRating" | "bookmarked" | "personalNotes">>
): Promise<ProgressView> {
  const { data } = await apiClient.put<ApiResponse<ProgressView>>(`/me/progress/${questionId}`, update);
  return data.data;
}

export async function fetchDashboardSummary(): Promise<DashboardSummary> {
  const { data } = await apiClient.get<ApiResponse<DashboardSummary>>("/dashboard/summary");
  return data.data;
}

export async function fetchStudyPlan(): Promise<StudyPlan> {
  const { data } = await apiClient.get<ApiResponse<StudyPlan>>("/study-plan");
  return data.data;
}

export async function fetchAnalytics(): Promise<Analytics> {
  const { data } = await apiClient.get<ApiResponse<Analytics>>("/analytics");
  return data.data;
}

