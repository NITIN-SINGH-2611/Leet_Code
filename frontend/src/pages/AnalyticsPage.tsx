import {
  Bar,
  BarChart,
  CartesianGrid,
  Cell,
  Legend,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
  XAxis,
  YAxis,
} from "recharts";
import { useAsync } from "@/hooks/useAsync";
import { fetchAnalytics } from "@/lib/api";
import { Card } from "@/components/ui/Card";
import { Skeleton } from "@/components/ui/Skeleton";

const DIFFICULTY_COLORS: Record<string, string> = {
  EASY: "#34d399",
  MEDIUM: "#fbbf24",
  HARD: "#fb7185",
};

const TOPIC_COLORS = ["#3b6fed", "#34d399", "#fbbf24", "#fb7185", "#a78bfa", "#22d3ee"];

export default function AnalyticsPage() {
  const { data, loading, error } = useAsync(fetchAnalytics);

  if (loading) {
    return (
      <div className="space-y-6">
        <Skeleton className="h-8 w-48" />
        <Skeleton className="h-80" />
      </div>
    );
  }

  if (error || !data) {
    return (
      <Card className="border-rose-500/30 max-w-xl">
        <p className="text-rose-300 text-sm">Couldn't load analytics ({error}).</p>
      </Card>
    );
  }

  const difficultyData = data.difficultyBreakdown.map((d) => ({
    name: d.difficulty.charAt(0) + d.difficulty.slice(1).toLowerCase(),
    key: d.difficulty,
    solved: d.solved,
    remaining: d.total - d.solved,
  }));

  const topicData = data.topicMastery.map((t) => ({
    name: t.topicName,
    value: t.solvedQuestions,
  }));

  return (
    <div className="space-y-6">
      <div>
        <h1 className="text-2xl font-bold tracking-tight">Analytics</h1>
        <p className="text-slate-400 text-sm mt-1">
          {data.totalSolved} of {data.totalQuestionsInBank} questions solved overall.
        </p>
      </div>

      <Card>
        <h2 className="font-semibold mb-4">Solved by difficulty</h2>
        <div className="h-64">
          <ResponsiveContainer width="100%" height="100%">
            <BarChart data={difficultyData}>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(255,255,255,0.05)" />
              <XAxis dataKey="name" stroke="#94a3b8" fontSize={12} />
              <YAxis stroke="#94a3b8" fontSize={12} allowDecimals={false} />
              <Tooltip
                contentStyle={{ background: "#0f172a", border: "1px solid rgba(255,255,255,0.1)", borderRadius: 8 }}
              />
              <Legend />
              <Bar dataKey="solved" stackId="a" name="Solved">
                {difficultyData.map((entry) => (
                  <Cell key={entry.key} fill={DIFFICULTY_COLORS[entry.key]} />
                ))}
              </Bar>
              <Bar dataKey="remaining" stackId="a" name="Remaining" fill="rgba(255,255,255,0.08)" />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </Card>

      <Card>
        <h2 className="font-semibold mb-4">Solved questions by topic</h2>
        {topicData.every((t) => t.value === 0) ? (
          <p className="text-sm text-slate-500">Solve a few questions to see this chart fill in.</p>
        ) : (
          <div className="h-72">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie data={topicData} dataKey="value" nameKey="name" innerRadius={60} outerRadius={90} paddingAngle={2}>
                  {topicData.map((_, i) => (
                    <Cell key={i} fill={TOPIC_COLORS[i % TOPIC_COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip
                  contentStyle={{ background: "#0f172a", border: "1px solid rgba(255,255,255,0.1)", borderRadius: 8 }}
                />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        )}
      </Card>
    </div>
  );
}
