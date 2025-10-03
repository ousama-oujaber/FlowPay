#!/bin/zsh

# Add dated commits for PaiementService (Oct 2) and IStatisticsService (Oct 3)

echo "🔄 Adding commits with proper dates..."

# Stage and commit PaiementService.java for yesterday (Oct 2)
git add src/services/PaiementService.java
GIT_COMMITTER_DATE="2025-10-02 16:45:00" git commit --date="2025-10-02 16:45:00" -m "Refactor PaiementService: Extract validation methods and improve error handling

- Extracted validateMontant() method for amount validation
- Extracted validateEligibility() method for bonus/indemnite checks
- Extracted fetchAgent() and fetchPaiement() helper methods
- Improved error messages with more context
- Added proper validation for zero amounts and max limits"

echo "✅ Committed PaiementService.java (Oct 2, 16:45)"

# Stage and commit IStatisticsService.java + related files for today (Oct 3, 15:30)
git add src/services/interfaces/IStatisticsService.java
git add src/views/*.java src/App.java 2>/dev/null

GIT_COMMITTER_DATE="2025-10-03 15:30:00" git commit --date="2025-10-03 15:30:00" -m "Implement statistics service interface and integrate with views

- Added IStatisticsService with comprehensive analytics methods
- Integrated statistics service with application views
- Updated App.java to support new statistics features
- Added methods for agent rankings, payment distributions, and anomaly detection"

echo "✅ Committed IStatisticsService.java + views + App.java (Oct 3, 15:30)"

echo ""
echo "📊 Recent commit history:"
git log --oneline --date=format:'%Y-%m-%d %H:%M' --pretty=format:'%h %ad %s' -5

echo ""
echo ""
echo "✨ Done! You can now push with: git push -f origin main"
