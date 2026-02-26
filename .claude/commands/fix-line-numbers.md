# Fix Line Numbers in Feature Text

Fix incorrect or missing line numbers in feature file text in Given step docstrings.

## Instructions

You are helping fix line numbers in feature file text blocks in Gherkin feature files.

### Task Steps

1. **Get the currently open file**
   - Use `mcp__jetbrains__get_all_open_file_paths` to find the active file
   - Verify it's a `.feature` file

2. **Read the file content**
   - Use `mcp__jetbrains__get_file_text_by_path` or `Read` tool

3. **Find Given step docstrings**
   - Look for patterns like:
     ```
     Given the following feature text:
       """
       <text content here>
       """
     ```

4. **Process all docstrings automatically**:
   - DO NOT ask user for confirmation
   - Process ALL "Given the following feature text:" docstrings in the file
   - Apply fixes to every occurrence automatically

5. **Process each docstring**:
   - Extract text between `"""` markers
   - Detect line number issues:
     * Missing line numbers on some lines
     * Incorrect starting index (0-indexed instead of 1-indexed)
     * Incorrect alignment or formatting
     * Gaps or duplicates in sequence
   - Fix the line numbers:
     * Strip existing line numbers (pattern: `^\s{0,4}\d+:`)
     * Re-add properly formatted 1-indexed line numbers
     * Right-align numbers based on total line count
     * Format: ` 1:`, ` 2:`, ..., ` 9:`, `10:`, `11:`, etc.
     * Preserve ALL original indentation in the text (after removing old line numbers)
   - Use `mcp__jetbrains__replace_text_in_file` to update each docstring

6. **Confirm completion**
   - Report which scenarios were fixed
   - Show an example of the transformation (before/after)

### Line Number Formatting Rules

- **1-indexed**: First line is `1:`, not `0:`
- **Right-aligned padding**:
  - 1-9 lines: ` 1:`, ` 2:`, etc. (1 space padding)
  - 10-99 lines: ` 1:`, ` 2:`, ... ` 9:`, `10:`, etc. (variable padding)
  - 100-999 lines: `  1:`, `  2:`, ... ` 99:`, `100:`, etc.
  - 1000-9999 lines: `   1:`, ... `9999:` (max 4 digits)
- **Colon alignment**: The `:` character should align vertically

### Common Issues to Fix

1. **0-indexed instead of 1-indexed**:
   ```
   0:Feature: test
   1:Scenario: test
   ```
   → Should be `1:` and `2:`

2. **Misaligned numbers**:
   ```
   1:Feature: test
   2:Scenario: one
   ...
   9:Scenario: nine
   10:Scenario: ten   # Alignment breaks here
   ```
   → Fix alignment for consistency

3. **Missing line numbers on some lines**:
   ```
   1:Feature: test
   2:
     Scenario: test   # Missing line number
   4:  Given step
   ```

4. **Incorrect sequence or gaps**:
   ```
   1:Feature: test
   2:Scenario: one
   4:Scenario: two   # Should be 3:
   ```

### Example Transformations

**BEFORE (0-indexed):**
```gherkin
Scenario: test folding
  Given the following feature text:
    """
    0:Feature: feature name
    1:
    2:  Scenario: scenario name
    3:    Given some precondition
    4:    When some action is taken
    5:    Then some outcome is expected
    """
```

**AFTER (1-indexed):**
```gherkin
Scenario: test folding
  Given the following feature text:
    """
     1:Feature: feature name
     2:
     3:  Scenario: scenario name
     4:    Given some precondition
     5:    When some action is taken
     6:    Then some outcome is expected
    """
```

**BEFORE (misaligned):**
```gherkin
Given the following feature text:
  """
  1:Feature: test
  2:Scenario: one
  ...
  9:Scenario: nine
  10:Scenario: ten
  """
```

**AFTER (properly aligned):**
```gherkin
Given the following feature text:
  """
   1:Feature: test
   2:Scenario: one
  ...
   9:Scenario: nine
  10:Scenario: ten
  """
```

**BEFORE (missing/mixed):**
```gherkin
Given the following feature text:
  """
  1:Feature: test
  2:
    Scenario: test
  4:  Given step
  """
```

**AFTER (fixed):**
```gherkin
Given the following feature text:
  """
  1:Feature: test
  2:
  3:  Scenario: test
  4:    Given step
  """
```

### Important Notes

- Only process `Given the following feature text:` steps (not other Given steps)
- Strip ALL existing line numbers before re-adding them
- Do NOT modify the indentation of the original text content (after stripping line numbers)
- Do NOT modify the `"""` delimiters or their indentation
- Process multiple docstrings in a single file if requested
- Use the IntelliJ file replacement API for atomic updates

### Algorithm for Fixing Line Numbers

```python
def fix_line_numbers(text_with_line_nums, start_line=1):
    # Step 1: Strip existing line numbers
    lines = text_with_line_nums.splitlines()
    stripped_lines = []
    for line in lines:
        # Remove line number pattern: optional spaces + digits + colon
        match = re.match(r'^\s{0,4}\d+:', line)
        if match:
            # Remove the line number part, keep the rest
            stripped = line[match.end():]
        else:
            stripped = line
        stripped_lines.append(stripped)

    # Step 2: Re-add properly formatted line numbers
    max_line_num = start_line + len(stripped_lines) - 1
    padding = len(str(max_line_num))  # Number of digits

    numbered_lines = []
    for i, line in enumerate(stripped_lines):
        line_num = start_line + i
        # Right-align the number, then add colon
        numbered_line = f"{line_num:>{padding}}:{line}"
        numbered_lines.append(numbered_line)

    return '\n'.join(numbered_lines)
```

## Success Criteria

- All selected docstrings have properly formatted line numbers
- Line numbers are 1-indexed (starting at 1)
- Colons are vertically aligned
- No missing or duplicate line numbers
- Original text indentation is preserved (after stripping old numbers)
- File is updated in IntelliJ IDEA
