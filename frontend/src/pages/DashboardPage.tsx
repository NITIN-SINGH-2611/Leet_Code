import { Flame, Target, TrendingUp, Trophy } from "lucide-react";
import { useAsync } from "@/hooks/useAsync";
import { fetchDashboardSummary } from "@/lib/api";
import { Card } from "@/components/ui/Card";
import { ProgressBar } from "@/components/ui/ProgressBar";
import { Skeleton } from "@/components/ui/Skeleton";
import { ActivityHeatmap } from "@/components/ui/ActivityHeatmap";

export default function DashboardPage() {
  const { data, loading, error } = useAsync(fetchDashboardSummary);

  if (loading) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-8 w-48" />
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {Array.from({ length: 4 }).map((_, i) => (
            <Skeleton key={i} className="h-20" />
          ))}
        </div>
        <Skeleton className="h-40" />
      </div>
    );
  }

  if (error || !data) {
    return (
      <Card className="border-rose-500/30 max-w-xl">
        <p className="text-rose-300 text-sm">Couldn't load your dashboard ({error}).</p>
      </Card>
    );
  }

  const statCards = [
    { label: "Questions Solved", value: String(data.questionsSolved), icon: Target, tone: "text-brand-300" },
    { label: "Current Streak", value: `${data.currentStreak} days`, icon: Flame, tone: "text-orange-400" },
    { label: "Accuracy", value: `${data.accuracyPercent}%`, icon: TrendingUp, tone: "text-emerald-400" },
    { label: "XP Level", value: String(data.level), icon: Trophy, tone: "text-amber-400" },
  ];

  const toneForIndex = (i: number) => (["emerald", "brand", "amber"] as const)[i % 3];

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Welcome back</h1>
        <p className="text-slate-400 text-sm mt-1">Here's where your interview prep stands today.</p>
      </div>

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        {statCards.map(({ label, value, icon: Icon, tone }) => (
          <Card key={label} className="flex items-center gap-4">
            <div className={`w-10 h-10 rounded-lg bg-white/5 flex items-center justify-center ${tone}`}>
              <Icon className="w-5 h-5" />
            </div>
            <div>
              <p className="text-xs text-slate-500">{label}</p>
              <p className="text-lg font-semibold">{value}</p>
            </div>
          </Card>
        ))}
      </div>

      <Card>
        <div className="flex items-center justify-between mb-4">
          <h2 className="font-semibold">Last 30 days</h2>
          <span className="text-xs text-slate-500">{data.xpToNextLevel} XP to level {data.level + 1}</span>
        </div>
        <ActivityHeatmap data={data.activityHeatmap} />
      </Card>

      <Card>
        <h2 className="font-semibold mb-4">Topic mastery</h2>
        {data.topicMastery.length === 0 ? (
          <p className="text-sm text-slate-500">
            No questions in the bank yet — check back once the curated question set lands.
          </p>
        ) : (
          <div className="space-y-3">
            {data.topicMastery.map((t, i) => (
              <div key={t.topicName}>
                <div className="flex justify-between text-xs text-slate-400 mb-1">
                  <span>{t.topicName}</span>
                  <span>
                    {t.solvedQuestions}/{t.totalQuestions} ({t.masteryPercent}%)
                  </span>
                </div>
                <ProgressBar value={t.masteryPercent} tone={toneForIndex(i)} />
              </div>
            ))}
          </div>
        )}
      </Card>
    </div>
  );
}
