---
description: Improve user story in feature file header
allowed-tools: Read, Edit, Grep, mcp__jetbrains__get_all_open_file_paths
---

Improve the user story text in a feature file header to make it more compelling with:
1. **FIRST:** Ensure the Ability:/Feature: keyword line has NO text after it (title is inferred from filename)
2. More plausible and specific motivation for the feature
3. Clearer and more impactful benefits to the user/persona
4. Better alignment with actual rules and scenarios in the feature file, if any
5. Proper user story format (As a... I want... So that...)

Usage:
- `/improve-story` - Improve the user story in the currently open feature file
- `/improve-story <file_path>` - Improve the user story in the specified feature file

## Instructions

1. **Identify the target feature file:**
   - Use the currently open file if no argument provided
   - Use the provided file path if specified
   - Read the entire feature file to understand its content

2. **MANDATORY: Ensure the Feature/Ability keyword line is empty:**
   - Find the "Ability:" or "Feature:" keyword at the top of the file
   - **CRITICAL REQUIREMENT:** The keyword line must have NO text after the colon
   - Example: The line should be just `Ability:` or `Feature:` with nothing following
   - **If there is any text after the keyword, remove it IMMEDIATELY before proceeding**
   - The title/name is inferred from the filename by the test framework
   - **NEVER add text to the keyword line** - it must always be empty

3. **Locate the user story section:**
   - Extract ONLY the description lines that follow the Feature/Ability line (typically 3-5 lines in "As a... I want... So that..." format)
   - Note the current persona, action, and benefit
   - These are the ONLY lines that should be improved

4. **Analyze the feature content:**
   - Read through all rules and scenarios in the feature file if present
   - Understand what the feature actually does based on the test cases if present, and if not present then extrapolate from the title and description
   - Identify the core functionality being tested
   - Note any Rules that provide context about the feature's behavior

5. **Improve the user story:**
   Focus on making it more compelling by:

   **Persona (As a...):**
   - Be specific about the user role (e.g., "content creator", "QA engineer", "developer writing BDD tests")
   - Consider who would actually use this feature based on the rules and scenarios if present, if not then extrapolate from the title and description

   **Motivation (I want to...):**
   - Make it specific and action-oriented
   - Align with what the scenarios actually test
   - Use active, concrete verbs
   - Focus on the capability, not just the UI action

   **Benefit (So that...):**
   - Make it outcome-focused and measurable
   - Explain the real-world value and impact
   - Be specific about productivity gains, error reduction, or improved experience
   - Connect to broader goals (efficiency, quality, collaboration, etc.)

6. **Write the improved user story:**
   - Maintain the same format structure
   - Keep indentation consistent (2 spaces for each line of the user story description)
   - Make it concise but impactful (1-2 lines per section)
   - Ensure it flows naturally when read aloud

7. **Update the feature file:**
   - **PRESERVE** the Feature:/Ability: keyword line as empty (no text after the colon)
   - If the keyword line has text after it, remove that text FIRST
   - Then, use the Edit tool to replace ONLY the old user story description lines (As a... I want... So that...)
   - Do NOT add any text to the keyword line during improvement
   - Maintain proper indentation (2 spaces for user story lines)

   Example structure:
   ```
   Ability:                               ← NEVER add text here (must be empty)
     As a content creator...              ← Improve these lines
     I want to...                         ← Improve these lines
     So that...                           ← Improve these lines
   ```

## Quality Checklist

Before finalizing, ensure:
- [ ] **Keyword line** (Feature:/Ability:) has NO text after the colon (title is inferred from filename)
- [ ] Keyword line was NOT modified during improvement (only cleared if it had text)
- [ ] Persona is specific and matches the actual users
- [ ] Motivation is action-oriented and clear
- [ ] Benefit includes specific outcomes or metrics when possible
- [ ] The story aligns with what rules and scenarios describe and actually test if present in the feature file
- [ ] Language is concise and impactful
- [ ] No jargon without context
- [ ] It answers "why should someone care about this feature?"
