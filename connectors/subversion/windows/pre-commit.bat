rem Arguments of the pre-commit hook
set REPOS=%1
set TXN="%2
echo Calling pre-commit hook on %REPOS% for transaction %TXN%

rem Calling the pre-commit script
python %REPOS%\hooks\cipol_pre_commit.py %REPOS% %TXN%
