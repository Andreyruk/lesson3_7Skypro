package pro.sky.lesson3_7skypro.services.Impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;
import pro.sky.lesson3_7skypro.model.Ingredients;
import pro.sky.lesson3_7skypro.model.Recipes;
import pro.sky.lesson3_7skypro.services.IngredientService;
import pro.sky.lesson3_7skypro.services.RecipesService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RecipesServiceImpl implements RecipesService {
    private TreeMap<Integer, Recipes> recipesMap = new TreeMap<>();
    private final IngredientService ingredientService;
    private static int id;

    @Override
    public int addRecipe(Recipes recipe) {
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            recipe.getIngredients().forEach(ingredientService::addIngredient);
        }
        recipesMap.put(id++, recipe);
        return id;
    }
    @Override
    public Recipes getRecipe(int id) {
        if (!recipesMap.containsKey(id)) {
            throw new NotFoundException("Рецепт с заданным id не найден");
        }
        return recipesMap.get(id);
    }
    
    @Override
    public Collection<Recipes> getAllRecipe() {
        return recipesMap.values();
    }
    
    @Override
    public Recipes editRecipe(int id, Recipes recipes){
        if (!recipesMap.containsKey(id)) {
            throw new NotFoundException("Рецепт с заданным id не найден");
        }
        return recipesMap.put(id, recipes);
    }

    @Override
    public Recipes removeRecipe(int id) {
        if (!recipesMap.containsKey(id)) {
            throw new NotFoundException("Рецепт с заданным id отсутствует");
        }
        return recipesMap.remove(id);
    }

    @Override
    public byte[] downloadRecipes() {
        String text = "";
        XWPFDocument document = new XWPFDocument();
        for (Recipes item : recipesMap.values()) {

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText(item.getNameRecipe());
            titleRun.setColor("FF0000");
            titleRun.setBold(true);
            titleRun.setFontFamily("Courier");
            titleRun.setFontSize(20);

            XWPFParagraph title1 = document.createParagraph();
            title1.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun titleRun1 = title1.createRun();
            titleRun1.setText("Время приготовления: ");
            titleRun1.setColor("000000");
            titleRun1.setBold(true);
            titleRun1.setFontFamily("Courier");
            titleRun1.setFontSize(20);

            XWPFRun titleRun2 = title1.createRun();
            titleRun2.setText(String.valueOf(item.getCookingTime()));
            titleRun2.setColor("000000");
            titleRun2.setBold(false);
            titleRun2.setFontFamily("Courier");
            titleRun2.setFontSize(20);

            XWPFRun titleRun3 = title1.createRun();
            titleRun3.setText(" " + item.getMeasureUnitTime());
            titleRun3.setColor("000000");
            titleRun3.setBold(false);
            titleRun3.setFontFamily("Courier");
            titleRun3.setFontSize(20);

            XWPFParagraph title2 = document.createParagraph();
            title2.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun titleRun4 = title2.createRun();
            titleRun4.setText("Ингредиенты: ");
            titleRun4.setColor("000000");
            titleRun4.setBold(true);
            titleRun4.setFontFamily("Courier");
            titleRun4.setFontSize(20);

            if (!item.getIngredients().isEmpty()) {
                item.getIngredients().forEach(ing -> {
                    XWPFParagraph title3 = document.createParagraph();
                    title3.setAlignment(ParagraphAlignment.LEFT);
                    title3.setStyle("ListParagraph");
                    title3.setNumID(BigInteger.valueOf(1));
                    XWPFRun titleRun5 = title3.createRun();
                    titleRun5.setText(ing.getNameIngredient());
                    titleRun5.setColor("000000");
                    titleRun5.setBold(false);
                    titleRun5.setFontFamily("Courier");
                    titleRun5.setFontSize(20);

                    XWPFRun titleRun6 = title3.createRun();
                    titleRun6.setText(" - " + ing.getQuantity());
                    titleRun6.setColor("000000");
                    titleRun6.setBold(false);
                    titleRun6.setFontFamily("Courier");
                    titleRun6.setFontSize(20);

                    XWPFRun titleRun7 = title3.createRun();
                    titleRun7.setText(" " + ing.getMeasureUnit());
                    titleRun7.setColor("000000");
                    titleRun7.setBold(false);
                    titleRun7.setFontFamily("Courier");
                    titleRun7.setFontSize(20);
                });
            }

            XWPFParagraph title4 = document.createParagraph();
            title4.setAlignment(ParagraphAlignment.LEFT);
            XWPFRun titleRun8 = title4.createRun();
            titleRun8.setText("Инструкция приготовления: ");
            titleRun8.setColor("000000");
            titleRun8.setBold(true);
            titleRun8.setFontFamily("Courier");
            titleRun8.setFontSize(20);

            if (!item.getStepsCooking().isEmpty()) {
                item.getStepsCooking().forEach(step -> {
                    XWPFParagraph title5 = document.createParagraph();
                    title5.setAlignment(ParagraphAlignment.LEFT);
                    title5.setStyle("ListParagraph");
                    XWPFRun titleRun9 = title5.createRun();
                    titleRun9.setText(step);
                    titleRun9.setColor("000000");
                    titleRun9.setBold(false);
                    titleRun9.setFontFamily("Courier");
                    titleRun9.setFontSize(12);
                });
            }

        }
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        try {
            document.write(ba);
            ba.close();
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ba.toByteArray();
    }
}
