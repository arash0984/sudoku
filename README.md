## Sudoku Solver

An Implementation Of Sudoku Solver For Android With Java

## images
<img src="https://github.com/arash0984/sudoku/blob/main/images/1.png" alt="image 1">
<img src="https://github.com/arash0984/sudoku/blob/main/images/2.png" alt="image 2">

### Step 1 (Main Activity)
- download games from https://sharetext.me/raw/zgzqhgvvqx
- split games numbers with "-" and add click event listener to buttons to start SudokuActivity
- pass game data to SudokuActivity by intent
### code:
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String games = null;
        try {
            games = Downloader.downloadGames("https://sharetext.me/raw/zgzqhgvvqx");
            startGame(games);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void startGame(String games){
        TextView loading = (TextView) findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        Button btn1 = (Button) findViewById(R.id.game1Btn);
        Button btn2 = (Button) findViewById(R.id.game2Btn);
        Button btn3 = (Button) findViewById(R.id.game3Btn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SudokuActivity.class);
                intent.putExtra("game", getGame(games, 0));
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SudokuActivity.class);
                intent.putExtra("game", getGame(games, 1));
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SudokuActivity.class);
                intent.putExtra("game", getGame(games, 2));
                startActivity(intent);
            }
        });
    }

### Step 2 (Downloader)
- download games by OkHttp library
- implement Downloader class to implement download code
### Add Dependency
    dependencies {
        ...
        implementation 'com.squareup.okhttp3:okhttp:3.10.0'
    }
### Add Permission To AndroidManifest.xml:
    <?xml version="1.0" encoding="utf-8"?>
    <manifest xmlns:android="http://schemas.android.com/apk/res/android"
        ...
        <uses-permission android:name="android.permission.INTERNET"/>
        <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        ... 
    </manifest>

### code:
    public class Downloader {
        priva`te static final OkHttpClient client = new OkHttpClient();
         public static String downloadGames(String url) throws IOException {
            Request request = new Request.Builder()
                    .url(url)
                    .build();
    
            try (Response response = client.newCall(request).execute()) {
                return response.body().string();
            }
        }
    }


### Step 3 (Implement Sudoku Solver)
- implementation of sudoku solver 
#### code:
    public class SudokuSolver {

    
        static int N = 9;
        
        public static boolean solveSudoku(int grid[][], int row,
                                          int col) {
    
            if (row == N - 1 && col == N)
                return true;
    
            if (col == N) {
                row++;
                col = 0;
            }
            if (grid[row][col] != 0)
                return solveSudoku(grid, row, col + 1);
    
            for (int num = 1; num < 10; num++) {
    
                if (isSafe(grid, row, col, num)) {
                    grid[row][col] = num;
                    if (solveSudoku(grid, row, col + 1))
                        return true;
                }
                grid[row][col] = 0;
            }
            return false;
        }
    
    
        static void print(int[][] grid) {
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++)
                    System.out.print(grid[i][j] + " ");
                System.out.println();
            }
        }
        static boolean isSafe(int[][] grid, int row, int col,
                              int num) {
        for (int x = 0; x <= 8; x++)
                if (grid[row][x] == num)
                    return false;
            for (int x = 0; x <= 8; x++)
                if (grid[x][col] == num)
                    return false;
            int startRow = row - row % 3, startCol
                    = col - col % 3;
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (grid[i + startRow][j + startCol] == num)
                        return false;
    
            return true;
        }
    }


### Step 4 (Sudoku Activity)
- get game data from intent
- instantiate a Config object and pass the game data to constructor method
- instantiate an object of GameView class (extends android View Class)
- add this object as view to the layouts linear layout
#### code:
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String game = ((Intent) intent).getStringExtra("game");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        view = new GameView(this, new Config(game));
        LinearLayout layout = (LinearLayout) findViewById(R.id.game_layout);
        layout.removeAllViews();
        layout.addView(view);
    }

### Step 5 (GameView implementation)
#### Methods:
#### constructor method:
    public GameView(Context context, Config config) {
        super(context);
        width = getResources().getDisplayMetrics().widthPixels;
        height = getResources().getDisplayMetrics().heightPixels;
        this.config = config;
        this.grid = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = config.getGrid1()[i][j];
            }
        }
    }
<p>
this code gets the device width and height to create view responsive
and create a two-dimensional array to keep track of the game 
</p>

#### drawHeader method:
    private void drawHeader(Canvas c) {
        int topEnd = headerTopStart + headerLineHeight;
        int topStartElement = headerTopStart + 2 * gap;
        int topEndElement = topEnd - 2 * gap;
        int useWidth = width - 8 * gap;
        int widthRect = (int) (useWidth * 0.33);
        int s1 = 2 * gap;
        int e1 = s1 + widthRect;
        int s2 = e1 + gap * 2;
        int e2 = s2 + widthRect;
        int textTopStart = (topStartElement + topEndElement) / 2 + 2 * gap;
        Paint p = new Paint();
        p.setColor(Color.parseColor("#2e75b6"));
        c.drawRect(0, headerTopStart, width, topEnd, p);
        // draw text
        p.setColor(Color.parseColor("#ced63c"));
        p.setTextSize(64);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(8);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.parseColor("#FFFFFF"));
        c.drawText("Time: " + counter, s2 + gap, textTopStart, p);
    }
<p>
    this code used to draw view's header and show the timer
    so we use drawRect() and drawText() on Canvas object and a Paint object to set style (Color, FontSize)
</p>

#### drawTable method:
    private void drawTable(Canvas c) {
        int sizeOfCell = getSizeOfCell();
        Paint p = new Paint();
        p.setStrokeWidth(6);
        for (int i = 0; i <= 9; i++) {
            c.drawLine(gap * 2 + i * sizeOfCell, tableTopStart, gap * 2 + i * sizeOfCell, tableTopStart + sizeOfCell * 9, p);
        }

        for (int i = 0; i <= 9; i++) {
            c.drawLine(gap * 2, tableTopStart + i * sizeOfCell, width - gap * 2, tableTopStart + i * sizeOfCell, p);
        }
    }
<p>
    this method used to draw sudoku table by calling drawLine method on Canvas object
</p>

#### drawBackground method:
    private void drawBackground(Canvas c) {
        Paint p = new Paint();
        p.setColor(Color.parseColor("#00ff00"));
        c.drawRect(gap * 2 + getSizeOfCell() * 3, tableTopStart, gap * 2 + getSizeOfCell() * 6,
                tableTopStart + getSizeOfCell() * 3, p);
        c.drawRect(gap * 2, tableTopStart + getSizeOfCell() * 3, gap * 2 + getSizeOfCell() * 3,
                tableTopStart + getSizeOfCell() * 6, p);
        c.drawRect(gap * 2 + getSizeOfCell() * 6, tableTopStart + getSizeOfCell() * 3,
                gap * 2 + getSizeOfCell() * 9, tableTopStart + getSizeOfCell() * 6, p);
        c.drawRect(gap * 2 + getSizeOfCell() * 3, tableTopStart + getSizeOfCell() * 6,
                gap * 2 + getSizeOfCell() * 6, tableTopStart + getSizeOfCell() * 9, p);
    }
<p>
this method used to set background color of sudoku table (green)
</p>

#### drawGridNumbers method:
    private void drawGridNumbers(Canvas c) {
        Paint p = new Paint();
        p.setTextSize(64);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                p.setColor(Color.parseColor("#000000"));
                int number = grid[i][j];
                String value = number == 0 ? " " : number + "";
                if (number != config.getGrid1()[i][j]) {
                    p.setColor(Color.parseColor("#ff0000"));
                }
                c.drawText(value, i * getSizeOfCell() + (float) (getSizeOfCell() / 2),
                        gap * 2 + j * getSizeOfCell() + (float) (getSizeOfCell() / 2) + tableTopStart, p);
            }
        }
    }
<p>
    this method used to print sudoku numbers on the table
and set color black for the initial state numbers
and red for solved puzzle numbers
</p>

#### hasNext method:
    private void hasNext() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    return;
                }
            }
        }
        stoped = true;
    }
<p>
    this method used to check if game has been completed or not
    if there is any 0 on the puzzle the game is not completed yet, after the game has been completed and all the numbers has been printed on the puzzle the timer will be stopped
</p>

#### updateGrid method:
    private void updateGrid() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = config.getGrid2()[i][j];
                    return;
                }
            }
        }
    }
<p>
    this method used in every interval to update grid array which keeps the game state at every interval
</p>

#### timer:
    private void timer(Canvas c) {
        startTimerThread = true;
        int speed = 500;
        Handler handler = new Handler();
        Runnable runner = new Runnable() {
            @Override
            public void run() {
                if (stoped) {
                    return;
                }
                counter++;
                drawGridNumbers(c);
                hasNext();
                updateGrid();
                invalidate();
                handler.postDelayed(this, speed);
            }
        };
        handler.postDelayed(runner, speed * 3);
    }
<p>
this method used to implement interval and set delay between states, we use Handler and Runnable class to create new thread to work in the background, in every interval counter increased by one and then draw grid numbers on the puzzle then update grid state to the new one and call invalidate() method to refresh Canvas and show the changes
</p>

### Config Class:
    public class Config {
        private final int[][] grid1;
        private final int[][] grid2;
        
    
        public Config(String game){
            String[] gameNumbers = game.split(",");
            grid1 = new int[9][9];
            grid2 = new int[9][9];
            for (int i = 0; i < gameNumbers.length; i++) {
                int number = Integer.parseInt(gameNumbers[i]);
                grid1[i / 9][i % 9] = number;
                grid2[i / 9][i % 9] = number;
            }
            SudokuSolver.solveSudoku(grid2, 0,0);
        }
        public int[][] getGrid1() {
            return this.grid1;
        }
        public int[][] getGrid2(){
            return this.grid2;
        }
    }

<p>
    config class used to keep the game's initial state and complete state.
GameView class uses this config to print the numbers on the puzzle
</p>


