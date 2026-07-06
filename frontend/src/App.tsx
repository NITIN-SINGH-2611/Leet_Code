import { useEffect } from "react";
import { Routes, Route } from "react-router-dom";
import { AppShell } from "@/components/layout/AppShell";
import { ProtectedRoute } from "@/components/layout/ProtectedRoute";
import { useAuthStore } from "@/store/authStore";
import DashboardPage from "@/pages/DashboardPage";
import TopicsPage from "@/pages/TopicsPage";
import QuestionsPage from "@/pages/QuestionsPage";
import QuestionDetailPage from "@/pages/QuestionDetailPage";
import StudyPlanPage from "@/pages/StudyPlanPage";
import AnalyticsPage from "@/pages/AnalyticsPage";
import LoginPage from "@/pages/auth/LoginPage";
import SignupPage from "@/pages/auth/SignupPage";
import ForgotPasswordPage from "@/pages/auth/ForgotPasswordPage";
import ResetPasswordPage from "@/pages/auth/ResetPasswordPage";

export default function App() {
  const bootstrap = useAuthStore((s) => s.bootstrap);

  useEffect(() => {
    bootstrap();
  }, [bootstrap]);

  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/forgot-password" element={<ForgotPasswordPage />} />
      <Route path="/reset-password" element={<ResetPasswordPage />} />

      <Route element={<ProtectedRoute />}>
        <Route element={<AppShell />}>
          <Route index element={<DashboardPage />} />
          <Route path="topics" element={<TopicsPage />} />
          <Route path="questions" element={<QuestionsPage />} />
          <Route path="questions/:slug" element={<QuestionDetailPage />} />
          <Route path="study-plan" element={<StudyPlanPage />} />
          <Route path="analytics" element={<AnalyticsPage />} />
        </Route>
      </Route>
    </Routes>
  );
}
