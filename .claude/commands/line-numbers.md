# Add Line Numbers and Fix Indentation in Feature Text

Add 1-indexed line numbers and correct indentation for feature file text in docstring arguments.

## Instructions

You are helping add line numbers and fix indentation for feature file text blocks in Gherkin feature files.

### Task Steps

1. **Get the currently open file**
   - Use `mcp__jetbrains__get_all_open_file_paths` to find the active file
   - Verify it's a `.feature` file

2. **Read the file content**
   - Use `mcp__jetbrains__get_file_text_by_path` or `Read` tool

3. **Find step docstrings**
   - Look for patterns like:
     ```
     Given the following feature text:
       """
       <text content here>
       """
     ```
   - Also works with `When I enter the following text into the editor:` and similar patterns

4. **Process ALL docstrings without line numbers**:
   - By default, process all docstrings following keywords like:
     * `Given the following feature text:`
     * `When I enter the following text into the editor:`
     * Any step with a `"""` docstring argument
   - Skip docstrings that already have line numbers (pattern: `^\s{0,4}\d+:`)
   - No need to ask user - just process all that need line numbers

5. **Process each docstring**:
   - Find the keyword line (e.g., `Given`, `When`, `Then`) and calculate its indentation level
   - Extract text between `"""` markers
   - **CRITICAL: Fix indentation for BOTH the `"""` delimiters AND the content**:
     * The `"""` opening and closing delimiters MUST be indented exactly 2 spaces relative to the keyword line
     * Content lines MUST ALSO be indented exactly 2 spaces relative to the keyword line (same as delimiters)
     * Find the minimum indentation of non-empty lines in the original content
     * Calculate target indentation: keyword_indent + 2
     * Adjust ALL lines (including moving the `"""` delimiters if needed) so everything is at the target indent
     * Preserve relative indentation between content lines
     * **This indentation fix is MANDATORY for every docstring, regardless of which step keyword it follows**
   - Add line numbers with correct padding:
     * Start at line 1 (not 0)
     * Right-align numbers based on total line count
     * **1-9 lines**: NO padding - format as `1:`, `2:`, etc. (NOT ` 1:`)
     * **10-99 lines**: Space padding for single digits - format as ` 1:`, ` 2:`, ..., ` 9:`, `10:`, `11:`, etc.
     * **100-999 lines**: Two-space padding - format as `  1:`, `  2:`, ..., ` 99:`, `100:`, etc.
   - Use `mcp__jetbrains__replace_text_in_file` to update each docstring

6. **Confirm completion**
   - Verify that indentation was fixed in ALL docstrings (not just line numbers added)
   - Report how many docstrings were updated
   - Confirm that both line numbers AND indentation corrections were applied

### Line Number Formatting Rules

- **1-indexed**: First line is `1:`, not `0:`
- **Right-aligned padding**:
  - 1-9 lines: `1:`, `2:`, etc. (no space padding)
  - 10-99 lines: ` 1:`, ` 2:`, ... ` 9:`, `10:`, etc. (variable padding)
  - 100-999 lines: `  1:`, `  2:`, ... ` 99:`, `100:`, etc.
  - 1000-9999 lines: `   1:`, ... `9999:` (max 4 digits)
- **Colon alignment**: The `:` character should align vertically

### Example Transformation

**BEFORE (incorrect indentation):**
```gherkin
Scenario: test folding
  Given the following feature text:
      """
Feature: feature name

  Scenario: scenario name
    Given some precondition
    When some action is taken
    Then some outcome is expected
      """
```

**AFTER (corrected indentation with line numbers):**
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

**Explanation:**
- The `Given` keyword is at 2 spaces
- The `"""` delimiters were at 6 spaces (incorrect) - moved to 4 spaces
- Content must be indented 2 spaces relative to keyword = 4 spaces total (same as delimiters)
- Original content started at 0 spaces (incorrect)
- Adjusted to 4 spaces and preserved relative indentation (Scenario at +2, steps at +4)
- Only 6 lines, so NO padding on line numbers (format: `1:`, not ` 1:`)

### Important Notes

- Process steps with docstring arguments (`"""` blocks), commonly:
  * `Given the following feature text:`
  * `When I enter the following text into the editor:`
  * `Then the visible editor text should be:` or `Then the editor's visible text should be:`
  * Any `Given`, `When`, `Then`, `And`, `But` step with `"""` docstrings
- Skip docstrings that already have line numbers (pattern: `^\s{0,4}\d+:`)
- **CRITICAL: ALWAYS fix indentation in ALL docstrings**:
  * The `"""` opening and closing delimiters MUST be at: keyword_indent + 2
  * Content lines MUST also be at: keyword_indent + 2 (same position as delimiters)
  * This applies to ALL steps: `Given`, `When`, `Then`, `And`, `But`, etc.
  * Never skip indentation correction, even if some docstrings appear correct
  * Calculate: target_indent = keyword_indent + 2, then adjust BOTH delimiters AND content to match
- Preserve relative indentation between lines within the content
- Process multiple docstrings in a single file if requested
- Use the IntelliJ file replacement API for atomic updates
- **Line number padding rules**:
  * Base padding on the number of lines IN THAT SPECIFIC docstring
  * 1-9 lines in docstring = no padding (`1:`, `2:`, etc.)
  * 10-99 lines in docstring = space padding (` 1:`, ` 2:`, ..., `10:`, `11:`, etc.)
  * 100-999 lines in docstring = two-space padding (`  1:`, `  2:`, ..., `100:`, etc.)
  * Each docstring's padding is independent of other docstrings in the same file

### Algorithm for Adding Line Numbers and Fixing Indentation

```python
def process_docstring(keyword_indent, content_text):
    """
    keyword_indent: number of spaces before the keyword (Given/When/Then)
    content_text: raw text between """ markers
    """
    lines = content_text.splitlines()

    # Step 1: Find minimum indentation of non-empty lines
    min_indent = float('inf')
    for line in lines:
        if line.strip():  # non-empty line
            leading_spaces = len(line) - len(line.lstrip())
            min_indent = min(min_indent, leading_spaces)

    if min_indent == float('inf'):
        min_indent = 0

    # Step 2: Calculate target base indentation
    # Content should be 2 spaces relative to keyword
    target_base_indent = keyword_indent + 2
    indent_adjustment = target_base_indent - min_indent

    # Step 3: Adjust indentation for all lines
    adjusted_lines = []
    for line in lines:
        if line.strip():  # non-empty line
            # Remove original indentation and add adjusted indentation
            content = line.lstrip()
            original_indent = len(line) - len(content)
            new_indent = original_indent + indent_adjustment
            adjusted_lines.append(' ' * new_indent + content)
        else:
            adjusted_lines.append('')  # empty lines stay empty

    # Step 4: Add line numbers
    max_line_num = len(adjusted_lines)
    padding = len(str(max_line_num))

    numbered_lines = []
    for i, line in enumerate(adjusted_lines):
        line_num = i + 1
        numbered_line = f"{line_num:>{padding}}:{line}"
        numbered_lines.append(numbered_line)

    return '\n'.join(numbered_lines)
```

## Success Criteria

- All selected docstrings have properly formatted line numbers
- Line numbers are 1-indexed
- Colons are vertically aligned
- Content indentation is corrected to be exactly 2 spaces relative to keyword line
- Relative indentation between lines is preserved
- File is updated in IntelliJ IDEA
