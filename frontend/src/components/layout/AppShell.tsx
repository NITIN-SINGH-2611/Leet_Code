import { Outlet } from "react-router-dom";
import { Sidebar } from "./Sidebar";
import { Topbar } from "./Topbar";

export function AppShell() {
  return (
    <div className="min-h-screen flex bg-[#FAF9F5] dark:bg-[#07090E] text-slate-800 dark:text-slate-100 transition-colors duration-300 relative overflow-hidden">
      {/* Ambient background glows */}
      <div className="absolute top-[-20%] left-[-10%] w-[600px] h-[600px] rounded-full ambient-glow-1 pointer-events-none z-0 orb-animation" />
      <div className="absolute bottom-[-10%] right-[-10%] w-[500px] h-[500px] rounded-full ambient-glow-2 pointer-events-none z-0 orb-animation" style={{ animationDelay: "-5s" }} />

      <Sidebar />
      <div className="flex-1 flex flex-col min-w-0 z-10">
        <Topbar />
        <main className="flex-1 overflow-y-auto p-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
