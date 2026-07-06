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
    <div className="space-y-8">
      {/* Premium Shopify-Editions Banner */}
      <div className="relative overflow-hidden rounded-xl2 glass-card p-6 md:p-8 flex flex-col md:flex-row items-center justify-between gap-6 border-brand-500/10 dark:border-white/[0.05]">
        <div className="space-y-3 z-10 text-left">
          <span className="text-[10px] font-semibold text-brand-600 dark:text-brand-300 uppercase tracking-widest bg-brand-500/10 dark:bg-brand-500/20 px-2.5 py-1 rounded-full">
            RenAIssance Edition
          </span>
          <h1 className="text-3xl md:text-4xl font-extrabold text-slate-800 dark:text-slate-100 font-sans">
            Elevate Your Coding IQ
          </h1>
          <p className="text-slate-500 dark:text-slate-400 text-sm max-w-md leading-relaxed">
            Master algorithmic patterns, build daily streaks, and crack technical interviews with AI-guided practice.
          </p>
        </div>
        
        {/* Animated Coding SVG Graphic */}
        <div className="w-full md:w-80 h-40 flex items-center justify-center relative select-none z-10 bg-slate-950/5 dark:bg-white/[0.02] rounded-xl border border-slate-200/50 dark:border-white/[0.03] p-2">
          <svg viewBox="0 0 400 200" className="w-full h-full text-brand-500 dark:text-brand-400">
            <defs>
              <pattern id="grid" width="20" height="20" patternUnits="userSpaceOnUse">
                <path d="M 20 0 L 0 0 0 20" fill="none" stroke="currentColor" strokeWidth="0.5" opacity="0.05" />
              </pattern>
              <linearGradient id="line-grad" x1="0%" y1="0%" x2="100%" y2="100%">
                <stop offset="0%" stopColor="rgb(59,111,237)" stopOpacity="0.8" />
                <stop offset="100%" stopColor="rgb(235,130,44)" stopOpacity="0.8" />
              </linearGradient>
            </defs>
            <rect width="100%" height="100%" fill="url(#grid)" />

            {/* Graph Connections */}
            <path d="M 200 40 L 120 90" stroke="currentColor" strokeWidth="1.5" opacity="0.3" strokeDasharray="5 5" />
            <path d="M 200 40 L 280 90" stroke="currentColor" strokeWidth="1.5" opacity="0.3" strokeDasharray="5 5" />
            <path d="M 120 90 L 70 140" stroke="currentColor" strokeWidth="1.5" opacity="0.3" />
            <path d="M 120 90 L 170 140" stroke="currentColor" strokeWidth="1.5" opacity="0.3" />
            <path d="M 280 90 L 230 140" stroke="currentColor" strokeWidth="1.5" opacity="0.3" />
            <path d="M 280 90 L 330 140" stroke="currentColor" strokeWidth="1.5" opacity="0.3" />
            
            {/* Active Traversal Path */}
            <path d="M 200 40 L 120 90 L 170 140" stroke="url(#line-grad)" strokeWidth="3" strokeLinecap="round" className="animate-draw-path" fill="none" />

            {/* Graph Nodes */}
            <g className="transition-transform duration-200">
              <circle cx="200" cy="40" r="16" fill="currentColor" className="text-slate-100 dark:text-slate-900" stroke="rgb(59,111,237)" strokeWidth="2" />
              <text x="200" y="44" textAnchor="middle" fontSize="9" className="fill-brand-600 dark:fill-brand-300 font-bold">Root</text>
            </g>
            
            <g className="transition-transform duration-200">
              <circle cx="120" cy="90" r="16" fill="currentColor" className="text-slate-100 dark:text-slate-900" stroke="rgb(59,111,237)" strokeWidth="2" />
              <text x="120" y="94" textAnchor="middle" fontSize="9" className="fill-brand-600 dark:fill-brand-300 font-bold">BFS</text>
            </g>
            
            <g className="transition-transform duration-200">
              <circle cx="280" cy="90" r="16" fill="currentColor" className="text-slate-100 dark:text-slate-900" stroke="currentColor" strokeWidth="2" opacity="0.6" />
              <text x="280" y="94" textAnchor="middle" fontSize="9" className="fill-slate-500 dark:fill-slate-400 font-bold">R1</text>
            </g>
            
            <circle cx="70" cy="140" r="12" fill="currentColor" className="text-slate-100 dark:text-slate-900" stroke="currentColor" strokeWidth="1.5" opacity="0.5" />
            <text x="70" y="143" textAnchor="middle" fontSize="8" className="fill-slate-500 dark:fill-slate-400">L2</text>

            <circle cx="170" cy="140" r="12" fill="rgb(235,130,44)" stroke="rgb(235,130,44)" strokeWidth="1.5" />
            <text x="170" y="143" textAnchor="middle" fontSize="8" fill="white" fontWeight="bold">DFS</text>
            
            <circle cx="230" cy="140" r="12" fill="currentColor" className="text-slate-100 dark:text-slate-900" stroke="currentColor" strokeWidth="1.5" opacity="0.5" />
            <circle cx="330" cy="140" r="12" fill="currentColor" className="text-slate-100 dark:text-slate-900" stroke="currentColor" strokeWidth="1.5" opacity="0.5" />
          </svg>
        </div>
      </div>

      {/* Stats Cards Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {statCards.map(({ label, value, icon: Icon, tone }) => (
          <Card key={label} className="flex items-center gap-4 border-slate-200/50 dark:border-white/[0.04] p-5 shadow-sm hover:translate-y-[-2px] transition-transform duration-200">
            <div className={`w-11 h-11 rounded-xl bg-slate-100 dark:bg-white/5 flex items-center justify-center ${tone}`}>
              <Icon className="w-5 h-5" />
            </div>
            <div>
              <p className="text-xs font-medium text-slate-400 dark:text-slate-500">{label}</p>
              <p className="text-xl font-bold text-slate-800 dark:text-slate-100 mt-0.5">{value}</p>
            </div>
          </Card>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Heatmap Card (takes 2 cols on lg screens) */}
        <div className="lg:col-span-2">
          <Card className="h-full border-slate-200/50 dark:border-white/[0.04]">
            <div className="flex items-center justify-between mb-6">
              <div>
                <h2 className="font-bold text-lg text-slate-800 dark:text-slate-100">Progress Heatmap</h2>
                <p className="text-xs text-slate-400 dark:text-slate-500 mt-0.5">Your daily activity over the last 30 days.</p>
              </div>
              <span className="text-xs font-semibold text-brand-600 dark:text-brand-400 bg-brand-500/10 px-3 py-1.5 rounded-lg">
                {data.xpToNextLevel} XP to level {data.level + 1}
              </span>
            </div>
            <ActivityHeatmap data={data.activityHeatmap} />
          </Card>
        </div>

        {/* Topic Mastery Card */}
        <Card className="border-slate-200/50 dark:border-white/[0.04]">
          <h2 className="font-bold text-lg text-slate-800 dark:text-slate-100 mb-1">Topic Mastery</h2>
          <p className="text-xs text-slate-400 dark:text-slate-500 mb-6">Mastery calculated from solved questions.</p>
          {data.topicMastery.length === 0 ? (
            <p className="text-sm text-slate-500">
              No questions in the bank yet — check back once the curated question set lands.
            </p>
          ) : (
            <div className="space-y-4 max-h-[300px] overflow-y-auto pr-1">
              {data.topicMastery.map((t, i) => (
                <div key={t.topicName} className="space-y-1.5">
                  <div className="flex justify-between text-xs font-medium text-slate-600 dark:text-slate-300">
                    <span>{t.topicName}</span>
                    <span className="text-slate-400 dark:text-slate-500">
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
    </div>
  );
}
