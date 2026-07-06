export type Difficulty = "EASY" | "MEDIUM" | "HARD";

export type ProgressStatus = "NOT_STARTED" | "ATTEMPTED" | "SOLVED" | "REVISION_DUE";

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data: T;
  timestamp: string;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

export interface Topic {
  id: number;
  name: string;
  slug: string;
  description?: string;
  sortOrder: number;
  questionCount: number;
}

export interface QuestionSummary {
  id: number;
  title: string;
  slug: string;
  difficulty: Difficulty;
  topicName: string;
  pattern: string;
  companyTags: string[];
  frequency: number;
  importanceScore: number;
}

export interface QuestionDetail extends QuestionSummary {
  topicId: number;
  timeComplexity?: string;
  spaceComplexity?: string;
  problemStatement?: string;
  hints?: string;
  editorial?: string;
  videoLink?: string;
}

export interface QuestionFilters {
  difficulty?: Difficulty;
  topicId?: number;
  company?: string;
  pattern?: string;
  search?: string;
  page?: number;
  size?: number;
}

export interface TopicMastery {
  topicName: string;
  totalQuestions: number;
  solvedQuestions: number;
  masteryPercent: number;
}

export interface DashboardSummary {
  questionsSolved: number;
  currentStreak: number;
  longestStreak: number;
  accuracyPercent: number;
  xp: number;
  level: number;
  xpToNextLevel: number;
  activityHeatmap: Record<string, number>;
  topicMastery: TopicMastery[];
}

export interface StudyPlan {
  targetQuestionsPerDay: number;
  todaysQuestions: QuestionSummary[];
  revisionDue: QuestionSummary[];
  solvedTotal: number;
  notStartedTotal: number;
}

export interface DifficultyStat {
  difficulty: Difficulty;
  total: number;
  solved: number;
}

export interface Analytics {
  difficultyBreakdown: DifficultyStat[];
  topicMastery: TopicMastery[];
  totalQuestionsInBank: number;
  totalSolved: number;
}
