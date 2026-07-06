import { HTMLAttributes } from "react";
import { cn } from "@/lib/cn";
import type { Difficulty } from "@/types";

interface BadgeProps extends HTMLAttributes<HTMLSpanElement> {
  tone?: "neutral" | "brand" | "easy" | "medium" | "hard";
}

const toneClasses: Record<NonNullable<BadgeProps["tone"]>, string> = {
  neutral: "bg-slate-100 dark:bg-white/10 text-slate-600 dark:text-slate-300",
  brand: "bg-brand-500/10 dark:bg-brand-500/15 text-brand-600 dark:text-brand-300",
  easy: "bg-emerald-500/10 dark:bg-emerald-500/15 text-emerald-700 dark:text-emerald-300",
  medium: "bg-amber-500/10 dark:bg-amber-500/15 text-amber-700 dark:text-amber-300",
  hard: "bg-rose-500/10 dark:bg-rose-500/15 text-rose-700 dark:text-rose-300",
};

export function Badge({ className, tone = "neutral", ...props }: BadgeProps) {
  return (
    <span
      className={cn(
        "inline-flex items-center px-2 py-0.5 rounded-md text-xs font-medium tracking-wide",
        toneClasses[tone],
        className
      )}
      {...props}
    />
  );
}

const difficultyTone: Record<Difficulty, BadgeProps["tone"]> = {
  EASY: "easy",
  MEDIUM: "medium",
  HARD: "hard",
};

export function DifficultyBadge({ difficulty }: { difficulty: Difficulty }) {
  return (
    <Badge tone={difficultyTone[difficulty]}>
      {difficulty.charAt(0) + difficulty.slice(1).toLowerCase()}
    </Badge>
  );
}
