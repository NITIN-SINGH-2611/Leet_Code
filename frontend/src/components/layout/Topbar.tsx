import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { LogOut, Moon, Search, Sun } from "lucide-react";
import { useUIStore } from "@/store/uiStore";
import { useAuthStore } from "@/store/authStore";
import { Input } from "@/components/ui/Input";

export function Topbar() {
  const { theme, toggleTheme } = useUIStore();
  const { user, logout } = useAuthStore();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  function handleLogout() {
    logout();
    navigate("/login");
  }

  const initial = user?.fullName?.charAt(0)?.toUpperCase() ?? "?";

  return (
    <header className="h-16 shrink-0 border-b border-slate-200 dark:border-white/10 flex items-center gap-4 px-5 bg-white/60 dark:bg-slate-950/60 backdrop-blur-md transition-colors duration-200">
      <div className="relative flex-1 max-w-md">
        <Search className="w-4 h-4 text-slate-400 absolute left-3 top-1/2 -translate-y-1/2" />
        <Input placeholder="Search questions, topics, companies..." className="pl-9" />
      </div>

      <div className="flex-1" />

      <button
        onClick={toggleTheme}
        aria-label="Toggle theme"
        className="w-9 h-9 rounded-lg flex items-center justify-center text-slate-500 dark:text-slate-400 hover:text-slate-900 dark:hover:text-slate-100 hover:bg-slate-100 dark:hover:bg-white/5 transition-colors"
      >
        {theme === "dark" ? <Sun className="w-4 h-4" /> : <Moon className="w-4 h-4" />}
      </button>

      <div className="relative">
        <button
          onClick={() => setMenuOpen((o) => !o)}
          className="w-9 h-9 rounded-full bg-brand-500/20 flex items-center justify-center text-sm font-semibold text-brand-300"
        >
          {initial}
        </button>
        {menuOpen && (
          <div className="absolute right-0 mt-2 w-48 glass-card p-2 z-10">
            <p className="px-2 py-1.5 text-xs text-slate-500 truncate">{user?.email}</p>
            <button
              onClick={handleLogout}
              className="w-full flex items-center gap-2 px-2 py-1.5 rounded-lg text-sm text-slate-300 hover:bg-white/5"
            >
              <LogOut className="w-3.5 h-3.5" />
              Log out
            </button>
          </div>
        )}
      </div>
    </header>
  );
}
