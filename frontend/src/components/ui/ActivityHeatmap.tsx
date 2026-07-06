import { cn } from "@/lib/cn";

interface HeatmapProps {
  data: Record<string, number>;
}

function intensityClass(count: number) {
  if (count === 0) return "bg-white/5";
  if (count === 1) return "bg-emerald-900";
  if (count === 2) return "bg-emerald-700";
  if (count === 3) return "bg-emerald-500";
  return "bg-emerald-400";
}

export function ActivityHeatmap({ data }: HeatmapProps) {
  const entries = Object.entries(data);

  return (
    <div className="flex flex-wrap gap-1">
      {entries.map(([date, count]) => (
        <div
          key={date}
          title={`${date}: ${count} question${count === 1 ? "" : "s"}`}
          className={cn("w-3.5 h-3.5 rounded-sm", intensityClass(count))}
        />
      ))}
    </div>
  );
}
