import { NavLink } from "react-router-dom";
import { LayoutDashboard, ListChecks, FolderKanban, CalendarClock, Flame, Code2, BarChart3 } from "lucide-react";
import { cn } from "@/lib/cn";
import { useAuthStore } from "@/store/authStore";

const NAV_ITEMS = [
  { to: "/", label: "Dashboard", icon: LayoutDashboard },
  { to: "/topics", label: "Topics", icon: FolderKanban },
  { to: "/questions", label: "Questions", icon: ListChecks },
  { to: "/study-plan", label: "Study Plan", icon: CalendarClock },
  { to: "/analytics", label: "Analytics", icon: BarChart3 },
];

export function Sidebar() {
  const user = useAuthStore((s) => s.user);
  const streak = user?.currentStreak ?? 0;

  return (
    <aside className="hidden md:flex md:flex-col w-64 shrink-0 border-r border-slate-200 dark:border-white/10 bg-white/60 dark:bg-slate-950/60 backdrop-blur-md transition-colors duration-200">
      <div className="flex items-center gap-2 px-5 h-16 border-b border-slate-200 dark:border-white/10">
        <Code2 className="w-5 h-5 text-brand-500 dark:text-brand-300" />
        <span className="font-semibold tracking-tight">CodeCrack AI</span>
      </div>

      <nav className="flex-1 px-3 py-4 space-y-1">
        {NAV_ITEMS.map(({ to, label, icon: Icon }) => (
          <NavLink
            key={to}
            to={to}
            end={to === "/"}
            className={({ isActive }) =>
              cn(
                "flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors",
                isActive
                  ? "bg-brand-500/15 text-brand-600 dark:text-brand-300"
                  : "text-slate-500 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-100 hover:bg-slate-100 dark:hover:bg-white/5"
              )
            }
          >
            <Icon className="w-4 h-4" />
            {label}
          </NavLink>
        ))}
      </nav>

      {/* Signature element: always-visible streak status, since the whole product
          is built around not breaking your daily prep habit. */}
      <div className="p-3">
        <div className="glass-card p-4 flex items-center gap-3">
          <div className="w-9 h-9 rounded-full bg-orange-500/15 flex items-center justify-center">
            <Flame className="w-4 h-4 text-orange-400" />
          </div>
          <div>
            <p className="text-sm font-semibold leading-none">{streak}-day streak</p>
            <p className="text-xs text-slate-500 mt-1">
              {streak > 0 ? "Keep it going today" : "Solve one today to start it"}
            </p>
          </div>
        </div>
      </div>
    </aside>
  );
}
