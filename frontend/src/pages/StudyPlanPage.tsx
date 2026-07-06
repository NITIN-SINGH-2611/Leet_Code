import { Link } from "react-router-dom";
import { CalendarClock, RotateCcw, Target } from "lucide-react";
import { useAsync } from "@/hooks/useAsync";
import { fetchStudyPlan } from "@/lib/api";
import { Card } from "@/components/ui/Card";
import { Skeleton } from "@/components/ui/Skeleton";
import { Badge, DifficultyBadge } from "@/components/ui/Badge";

export default function StudyPlanPage() {
  const { data, loading, error } = useAsync(fetchStudyPlan);

  if (loading) {
    return (
      <div className="space-y-4">
        <Skeleton className="h-8 w-64" />
        <Skeleton className="h-32" />
        <Skeleton className="h-32" />
      </div>
    );
  }

  if (error || !data) {
    return (
      <Card className="border-rose-500/30 max-w-xl">
        <p className="text-rose-300 text-sm">Couldn't load your study plan ({error}).</p>
      </Card>
    );
  }

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Study Plan</h1>
        <p className="text-slate-400 text-sm mt-1">
          Your target: {data.targetQuestionsPerDay} question{data.targetQuestionsPerDay === 1 ? "" : "s"}/day.{" "}
          {data.solvedTotal} solved so far, {data.notStartedTotal} left in the bank.
        </p>
      </div>

      <Card>
        <div className="flex items-center gap-2 mb-4">
          <Target className="w-4 h-4 text-brand-300" />
          <h2 className="font-semibold">Today's picks</h2>
        </div>
        {data.todaysQuestions.length === 0 ? (
          <p className="text-sm text-slate-500">
            You've solved everything in the bank — nice work. More questions land as the curated set grows.
          </p>
        ) : (
          <div className="space-y-2">
            {data.todaysQuestions.map((q) => (
              <Link key={q.id} to={`/questions/${q.slug}`}>
                <div className="flex items-center justify-between p-3 rounded-lg hover:bg-white/5 transition-colors">
                  <div className="flex items-center gap-2">
                    <DifficultyBadge difficulty={q.difficulty} />
                    <span className="text-sm font-medium">{q.title}</span>
                  </div>
                  <Badge tone="brand">{q.topicName}</Badge>
                </div>
              </Link>
            ))}
          </div>
        )}
      </Card>

      <Card>
        <div className="flex items-center gap-2 mb-4">
          <RotateCcw className="w-4 h-4 text-amber-400" />
          <h2 className="font-semibold">Revision due</h2>
        </div>
        {data.revisionDue.length === 0 ? (
          <p className="text-sm text-slate-500">Nothing due for review right now.</p>
        ) : (
          <div className="space-y-2">
            {data.revisionDue.map((q) => (
              <Link key={q.id} to={`/questions/${q.slug}`}>
                <div className="flex items-center justify-between p-3 rounded-lg hover:bg-white/5 transition-colors">
                  <div className="flex items-center gap-2">
                    <DifficultyBadge difficulty={q.difficulty} />
                    <span className="text-sm font-medium">{q.title}</span>
                  </div>
                  <CalendarClock className="w-4 h-4 text-slate-500" />
                </div>
              </Link>
            ))}
          </div>
        )}
      </Card>
    </div>
  );
}
