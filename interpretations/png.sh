# Little convenience script.
# Runs the procedural generator (on the input in the dev/input/ folder),
# then runs the minecraft.py interpreter (on the output in the dev/output folder)

(cd ../dev/ && java -jar Kukkura.jar)
python3 to_png.py

