import { cn } from "@/lib/cn";

interface ProgressBarProps {
  value: number; // 0-100
  className?: string;
  tone?: "brand" | "emerald" | "amber";
}

export function ProgressBar({ value, className, tone = "brand" }: ProgressBarProps) {
  const clamped = Math.max(0, Math.min(100, value));
  const toneClasses = {
    brand: "bg-brand-500",
    emerald: "bg-emerald-500",
    amber: "bg-amber-500",
  }[tone];

  return (
    <div className={cn("h-1.5 w-full rounded-full bg-white/10 overflow-hidden", className)}>
      <div
        className={cn("h-full rounded-full transition-all duration-500", toneClasses)}
        style={{ width: `${clamped}%` }}
      />
    </div>
  );
}
