# Collaboration Between Claude Code and OpenCode

## Communication Strategy for Joint Development

To enable effective collaboration between Claude Code and OpenCode on this Android project, we can establish the following communication and coordination mechanisms:

### 1. Shared Task Management
- Use a common task tracking system (like GitHub Issues or a shared TODO file)
- Each agent creates and claims tasks before working on them
- Tasks should have clear descriptions, acceptance criteria, and ownership

### 2. Code Review Process
- Both agents should review each other's code changes
- Use pull requests or a similar mechanism for code reviews
- Establish coding standards that both agents follow

### 3. Conflict Resolution Protocol
- When both agents want to modify the same file, use a locking mechanism
- If conflicts arise, prioritize based on task importance and dependencies
- Use git branching strategies to isolate changes

### 4. Progress Synchronization
- Regular status updates through a shared log or messaging system
- Milestone-based checkpoints to ensure alignment
- Daily stand-ups (automated) to discuss progress and blockers

### 5. Knowledge Sharing
- Maintain a shared knowledge base (like the CLAUDE.md and AGENTS.md files)
- Document decisions and rationales for future reference
- Share insights about the codebase structure and patterns

### 6. Testing Coordination
- Coordinate test execution to avoid conflicts
- Share test results and metrics
- Collaborate on test coverage improvement

### 7. Communication Channels
- Direct file-based communication (reading/writing to shared files)
- Git commit messages as a communication medium
- Structured comments in code for ongoing discussions
- GitHub Copilot as a real-time collaboration platform for code suggestions and discussions

### 8. Role Definition
- Define specific roles for each agent (e.g., one handles UI, the other backend logic)
- Specialize based on expertise areas
- Cross-train on critical components to ensure redundancy

### 9. Implementation of Collaboration Framework

#### Shared Documentation
- Maintain PROJECT_STATUS.md as single source of truth
- Include sections: Current Focus, Recently Completed, Up Next, Blocked Items

#### Task Coordination Through File Annotations
- Add comments in code files indicating which agent is working on what:
  ```kotlin
  // TODO(opencode): Implement background processing for calendar queries
  // TODO(claudecode): Optimize database query filtering
  ```

#### Status Check-ins
- Create .agent-status with timestamped entries:
  ```
  [2026-03-21 10:30] opencode: Working on performance optimization in CalendarTodayReader
  [2026-03-21 10:45] claudecode: Starting work on intent filter alignment
  ```

#### Branch-Based Workflow
- Use feature branches for major changes:
  - `opencode/performance-enhancements`
  - `claudecode/ui-improvements`
- Merge to main only after peer review simulation

#### Shared Task Board Simulation
- Maintain TASK_BOARD.md with columns:
  - Backlog
  - In Progress (with assignee names)
  - Review Needed
  - Completed

#### Conflict Resolution Protocol Enhancement
- When both agents want to modify the same file:
  1. Check PROJECT_STATUS.md for current owner
  2. If conflict persists, defer to project lead (human user)
  3. Document resolution in DECISION_LOG.md

### 10. Enhanced Daily Sync Protocol
At start of each session, check:
1. `PROJECT_STATUS.md` for updates
2. Git log for recent commits
3. Pending TODOs in codebase
4. TASK_BOARD.md for assignment conflicts

### 11. Communication Channels Hierarchy
1. **Primary**: Direct file annotations and documentation
2. **Secondary**: Comments in pull requests/commits
3. **Tertiary**: Status updates in shared markdown files

### 12. Handoff Protocol
When completing a task that another agent might work on next:
- Update PROJECT_STATUS.md with clear handoff notes
- Highlight any partially completed work or caveats
- Tag the other agent in TASK_BOARD.md

### 13. Quality Assurance Integration
- Each agent reviews the other's work before merging
- Maintain REVIEW_CHECKLIST.md for consistency
- Cross-validate performance improvements and bug fixes

This enhanced framework ensures efficient parallel work while avoiding conflicts and maintaining code quality, building upon the foundational collaboration principles already established.